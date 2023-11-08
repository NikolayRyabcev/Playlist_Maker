package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    val playlistList: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>()
    fun getPlaylists() {
        viewModelScope.launch {
            interactor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlistList.postValue(it)
                       // return@collect
                    } else {
                        playlistList.postValue(emptyList())
                    }
                }
        }
    }

}