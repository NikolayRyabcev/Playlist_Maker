package com.example.playlistmaker.data.playlistDataBase

import android.util.Log
import com.example.playlistmaker.App.PlayistDataBase
import com.example.playlistmaker.App.TrackInPlaylistDataBase
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playistDataBase: PlayistDataBase,
    private val converter: PlaylistConverter,
    private val trackInDataBase: TrackInPlaylistDataBase
) : PlaylistRepository {
    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val playlist = Playlist(
            playlistName,
            description,
            uri,
            emptyList(),
            0
        )
        playistDataBase.playlistDao().insertPlaylist(
            converter.mapplaylistClassToEntity(playlist)
        )
    }

    override fun deletePlaylist(item: Playlist) {
        converter.mapplaylistClassToEntity(item)?.let { playistDataBase.playlistDao().deletePlaylist(it) }
    }

    override fun queryPlaylist(): Flow<List<Playlist>> = flow {
        val playlistList = playistDataBase.playlistDao().queryPlaylist()
        if (playlistList.isEmpty()) emit(emptyList()) else {
            val playlistConverted =
                playistDataBase.playlistDao().queryPlaylist()
                    .map { converter.mapplaylistEntityToClass(it) }
            emit(playlistConverted)
        }
    }

    override fun update(track: Track, playlist: Playlist) {
        val tracklist = playlist.trackArray.toString()
        Log.d("Запись в плейлист", "пишем в репозиторий  $tracklist")
        playistDataBase.playlistDao().updatePlaylist(converter.mapplaylistClassToEntity(playlist))
        trackInDataBase.trackListingDao().insertTrack(track)
        val entityTrackList = converter.mapplaylistClassToEntity(playlist).trackList.toString()
        Log.d("Запись в плейлист", "в таблицу  $entityTrackList")
    }
}