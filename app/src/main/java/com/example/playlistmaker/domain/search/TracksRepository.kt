package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.search.request_and_response.Resource
import com.example.playlistmaker.domain.search.models.Track

interface TracksRepository {
    fun searchTracks (expression:String) :Resource<List<Track>>
}