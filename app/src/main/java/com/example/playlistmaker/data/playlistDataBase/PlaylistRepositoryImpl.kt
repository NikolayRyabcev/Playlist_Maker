package com.example.playlistmaker.data.playlistDataBase

import com.example.playlistmaker.App.PlayistDataBase
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val database: PlayistDataBase
) :PlaylistRepository{
    override fun addPlaylist(item:Playlist) {
        database.playlistDao().insertPlaylist(item)
    }

    override fun deletePlaylist(item:Playlist) {
        database.playlistDao().deletePlaylist(item)
    }

    override fun queryPlaylist() : Flow<List<Playlist>> = flow {
        val playlistList = database.playlistDao().queryPlaylist()
        if (playlistList.isEmpty()) emit (emptyList()) else emit(playlistList)
    }
}