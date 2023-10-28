package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {


    fun addPlayList(item: Playlist) {
        interactor.addPlaylist(item)
    }

    fun deletePlaylist(item: Playlist) {
        interactor.deletePlaylist(item)
    }


}