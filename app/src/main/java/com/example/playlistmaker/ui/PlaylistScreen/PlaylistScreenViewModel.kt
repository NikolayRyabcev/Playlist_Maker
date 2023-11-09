package com.example.playlistmaker.ui.PlaylistScreen

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractor

class PlaylistScreenViewModel(
    private val interactor: PlaylistScreenInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun sharePlaylist(playlist: Playlist) {
        interactor.sharePlaylist(playlist)
    }

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isAppThemeDark()
    }
}