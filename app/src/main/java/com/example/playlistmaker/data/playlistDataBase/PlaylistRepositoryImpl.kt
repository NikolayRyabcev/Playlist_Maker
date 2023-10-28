package com.example.playlistmaker.data.playlistDataBase

import com.example.playlistmaker.App.PlayistDataBase
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistRepository

class PlaylistRepositoryImpl(
    private val database: PlayistDataBase,
    private val converter:PlaylistConverter
) :PlaylistRepository{
    override fun addPlaylist(item:Playlist) {
        database.playlistDao().insertPlaylist(item)
    }

    override fun deletePlaylist(item:Playlist) {
        database.playlistDao().deletePlaylist(item)
    }

    override fun queryPlaylist() :List<Playlist>{
        val playlistList = database.playlistDao().queryPlaylist()
        if (playlistList.isNullOrEmpty()) return emptyList() else return playlistList
    }
}