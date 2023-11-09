package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor

class EditPlaylistViewModel(
    private val interactor: PlaylistInteractor,
) : ViewModel() {

    fun savePlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        interactor.addPlaylist(playlistName, description, uri)
    }
}

