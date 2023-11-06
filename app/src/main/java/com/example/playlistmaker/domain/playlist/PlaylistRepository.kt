package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun addPlaylist(playlistName: String,
                    description: String?,
                    uri: String)
    fun deletePlaylist(item:Playlist)
    fun queryPlaylist() : Flow<List<Playlist>>
    fun update(track: Track, playlist: Playlist)
}