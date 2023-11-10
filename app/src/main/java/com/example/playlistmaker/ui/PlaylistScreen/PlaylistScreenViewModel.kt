package com.example.playlistmaker.ui.PlaylistScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractor
import kotlinx.coroutines.launch

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

    val trackList : MutableLiveData <List <Track>> = MutableLiveData(emptyList())
    fun getTrackList (playlist: Playlist) {
        viewModelScope.launch {
            interactor.getTrackList(playlist).collect {
                list -> trackList.postValue(list)
            }
        }

    }
}