package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.search.requestAndResponse.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}