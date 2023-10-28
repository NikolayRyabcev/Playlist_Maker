package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun addPlaylist(item: Playlist)
    fun deletePlaylist(item: Playlist)
    fun queryPlaylist() : Flow<List<Playlist>>
}