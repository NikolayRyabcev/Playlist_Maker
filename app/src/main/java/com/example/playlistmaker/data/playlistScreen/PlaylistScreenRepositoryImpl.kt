package com.example.playlistmaker.data.playlistScreen

import android.app.Application
import android.content.Intent
import com.example.playlistmaker.App.TrackInPlaylistDataBase
import com.example.playlistmaker.R
import com.example.playlistmaker.data.favouritesDataBase.TrackConverter
import com.example.playlistmaker.data.trackInPlaylist.TrackInPlaylistDAO
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistScreenRepositoryImpl(
    private val application: Application,
    private val base:TrackInPlaylistDataBase
) : PlaylistScreenRepository {

    override fun sharePlaylist(playlist: Playlist) {
        val trackList: List<Track> = emptyList() //достать треки из плейлиста
        val nameOfPlaylist = playlist.playlistName
        val desriptionOfPlaylist = playlist.description
        val trackNumber = playlist.arrayNumber
        var trackInfo = "$nameOfPlaylist /n $desriptionOfPlaylist /n $trackNumber треков /n"
        trackList.forEach { track ->
            val i = trackList.indexOf(track)
            val name = track.trackName
            val duration = track.trackTimeMillis
            trackInfo = "$trackInfo $i. $name  - ($duration)"
        }
        if (trackNumber==0) trackInfo = "В этом плейлисте нет списка треков, которым можно поделиться"

        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, trackInfo)
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        playlist.trackArray.map {id ->
            val trackId = id ?: return@map
            val entity = base.trackListingDao().queryTrackId(searchId = trackId) ?: return@map
            TrackConverter().mapTrackEntityToTrack(entity)
        }
    }
}