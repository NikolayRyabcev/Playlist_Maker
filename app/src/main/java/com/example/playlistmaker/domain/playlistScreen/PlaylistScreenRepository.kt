package com.example.playlistmaker.domain.playlistScreen

import com.example.playlistmaker.domain.models.Playlist

interface PlaylistScreenRepository {
    fun sharePlaylist (playlist: Playlist)
}