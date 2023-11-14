package com.example.playlistmaker.domain.playlistScreen

import android.util.Log
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistScreenInteractorImpl(private val repository: PlaylistScreenRepository) :
    PlaylistScreenInteractor {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> {
        return repository.getTrackList(playlist)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> {
        return  repository.timeCounting(playlist)
    }
}