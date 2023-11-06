package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val repository: PlaylistRepository) : PlaylistInteractor {
    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        repository.addPlaylist(playlistName, description, uri)
    }

    override fun deletePlaylist(item: Playlist) {
        repository.deletePlaylist(item)
    }

    override fun queryPlaylist(): Flow<List<Playlist>> {
        return repository.queryPlaylist()
    }

    override fun update(track: Track, playlist: Playlist) {
        repository.update(track, playlist)
    }
}