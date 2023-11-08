package com.example.playlistmaker.domain.playlistScreen

import com.example.playlistmaker.domain.models.Playlist

interface PlaylistScreenInteractor {
    fun sharePlaylist (playlist: Playlist)
}