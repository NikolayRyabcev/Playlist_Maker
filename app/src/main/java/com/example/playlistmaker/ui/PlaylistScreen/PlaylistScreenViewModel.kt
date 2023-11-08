package com.example.playlistmaker.ui.PlaylistScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor

class PlaylistScreenViewModel(private val interactor: PlaylistScreenInteractor) : ViewModel() {

    fun sharePlaylist(playlist: Playlist) {
        interactor.sharePlaylist(playlist)
    }
}