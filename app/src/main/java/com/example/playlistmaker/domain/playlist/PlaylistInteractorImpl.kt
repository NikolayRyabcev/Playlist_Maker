package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
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

    override fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun update(playlist: Playlist) {
        repository.update(playlist)
    }
}