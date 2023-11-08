package com.example.playlistmaker.domain.playlistScreen

class PlaylistScreenInteractorImpl(private val repository: PlaylistScreenRepository) :
    PlaylistScreenInteractor {
    override fun sharePlaylist() {
        repository.sharePlaylist()
    }
}