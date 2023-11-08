package com.example.playlistmaker.domain.playlistScreen

import com.example.playlistmaker.domain.models.Playlist

class PlaylistScreenInteractorImpl(private val repository: PlaylistScreenRepository) :
    PlaylistScreenInteractor {
    override fun sharePlaylist(playlist: Playlist) {
        repository.sharePlaylist(playlist)
    }
}