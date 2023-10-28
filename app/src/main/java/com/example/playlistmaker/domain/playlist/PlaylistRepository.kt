package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist

interface PlaylistRepository {
    fun addPlaylist(item: Playlist)
    fun deletePlaylist(item:Playlist)
    fun queryPlaylist() :List<Playlist>
}