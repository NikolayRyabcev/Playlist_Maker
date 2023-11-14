package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor

class EditPlaylistViewModel(
    private val playlistInteractor1: PlaylistInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun savePlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playlistInteractor1.addPlaylist(playlistName, description, uri)
    }
    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }
}

