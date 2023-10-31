package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun addPlaylist(item: Playlist)
    fun deletePlaylist(item: Playlist)
    fun queryPlaylist() : Flow<List<Playlist>>
    fun addTrack(track: Track)
    fun update(track: Track, playlist: Playlist)
}