package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track

class TracksRepositoryImpl:TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = NetworkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode=200)
    }
}