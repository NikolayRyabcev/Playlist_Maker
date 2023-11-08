package com.example.playlistmaker.ui.PlaylistScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor

class PlaylistScreenViewModel (private val interactor: PlaylistScreenInteractor) : ViewModel() {

    fun sharePlaylist () {
        interactor.sharePlaylist()
    }
}