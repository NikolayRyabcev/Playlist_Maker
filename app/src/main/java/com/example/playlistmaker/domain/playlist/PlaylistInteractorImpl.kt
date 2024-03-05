package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistInteractorImpl @Inject constructor(val repository: PlaylistRepository) :
    PlaylistInteractor {
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

    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        repository.savePlaylist(
            playlist,
            playlistName,
            description,
            uri
        )
    }

    override fun findPlaylist(searchId: Int): Flow<Playlist> {
        return repository.findPlaylist(searchId)
    }
}