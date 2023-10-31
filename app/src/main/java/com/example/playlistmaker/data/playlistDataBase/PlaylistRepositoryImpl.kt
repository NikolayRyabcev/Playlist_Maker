package com.example.playlistmaker.data.playlistDataBase

import com.example.playlistmaker.App.PlayistDataBase
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val database: PlayistDataBase,
    private val converter: PlaylistConverter
) :PlaylistRepository{
    override fun addPlaylist(item:Playlist) {
        database.playlistDao().insertPlaylist(converter.mapplaylistClassToEntity(item))
    }

    override fun deletePlaylist(item:Playlist) {
        converter.mapplaylistClassToEntity(item)?.let{database.playlistDao().deletePlaylist(it)}
    }

    override fun queryPlaylist() : Flow<List<Playlist>> = flow {
        val playlistList = database.playlistDao().queryPlaylist()
        if (playlistList.isEmpty()) emit (emptyList()) else {
            val playlistConverted =
                database.playlistDao().queryPlaylist().map { converter.mapplaylistEntityToClass(it) }
            emit(playlistConverted)
        }
    }

    override fun update(playlist: Playlist) {
        database.playlistDao().updatePlaylist(converter.mapplaylistClassToEntity(playlist))
    }
}