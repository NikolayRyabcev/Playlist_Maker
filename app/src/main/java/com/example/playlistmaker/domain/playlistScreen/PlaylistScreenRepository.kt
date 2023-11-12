package com.example.playlistmaker.domain.playlistScreen

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistScreenRepository {
    fun sharePlaylist (playlist: Playlist)
    fun getTrackList(playlist: Playlist) : Flow<List<Track>>
    fun timeCounting (playlist: Playlist) : Flow<String>
}