package com.example.playlistmaker.data.playlistScreen

import android.util.Log
import com.example.playlistmaker.App.TrackInPlaylistDataBase
import com.example.playlistmaker.data.favouritesDataBase.TrackConverter
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistScreenRepositoryImpl(
    private val base: TrackInPlaylistDataBase
) : PlaylistScreenRepository {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        var trackList: List<Track> = emptyList()
        playlist.trackArray.map { id ->
            val trackId = id ?: return@map
            val entity = base.trackListingDao().queryTrackId(searchId = id) ?: return@map
            trackList = trackList + (TrackConverter().mapTrackEntityToTrack(entity))
        }
        Log.d("Треки", trackList.toString())
        emit(trackList)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> = flow {
        var generalTime = 0
        var trackSeconds = 0
        Log.d("Время массив", playlist.trackArray.toString())
        playlist.trackArray.forEach {
            val entity = it?.let { it1 -> base.trackListingDao().queryTrackId(searchId = it1) }
            val track = (entity?.let { it1 -> TrackConverter().mapTrackEntityToTrack(it1) })
            val time = track?.trackTimeMillis
            Log.d("Время время трека", time.toString())
            trackSeconds =
                (time?.split(":")?.get(0)?.toInt() ?: 0) * 60 + (time?.split(":")?.get(1)
                    ?.toInt()
                    ?: 0)
            generalTime += trackSeconds
            Log.d("Время rep", generalTime.toString())
        }
        val hours = generalTime / (60 * 60)
        val minutes = generalTime / 60
        val seconds = generalTime % 60
        val readyTime = if (hours == 0){
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        Log.d("Время rep", readyTime)
        emit(readyTime)
    }
}