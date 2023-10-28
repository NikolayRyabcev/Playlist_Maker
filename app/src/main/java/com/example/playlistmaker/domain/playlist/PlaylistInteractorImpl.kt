package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val repository: PlaylistRepository) : PlaylistInteractor {
    override fun addPlaylist(item: Playlist) {
        repository.addPlaylist(item)
    }

    override fun deletePlaylist(item: Playlist) {
        repository.deletePlaylist(item)
    }

    override fun queryPlaylist(): Flow<List<Playlist>> {
        return repository.queryPlaylist()
    }
}