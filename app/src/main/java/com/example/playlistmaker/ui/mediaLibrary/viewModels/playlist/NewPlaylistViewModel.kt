package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun addPlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        //в базе данных onConflict = OnConflictStrategy.REPLACE; «Не следует множить сущее без необходимости» (У. Оккам):)))
        interactor.addPlaylist(playlistName, description, uri)
    }

    fun deletePlaylist(item: Playlist) {
        interactor.deletePlaylist(item)
    }

    fun isAppThemeDark(): Boolean {
        return settingsInteractor.isAppThemeDark()
    }
}