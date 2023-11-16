package com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun savePlayList(
        playlist:Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playlistInteractor.savePlaylist(playlist, playlistName, description, uri)
    }
    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }
}

