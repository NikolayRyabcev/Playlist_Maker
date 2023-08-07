package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Track

interface SearchInteractor {
    fun search (expression:String, consumer:TracksConsumer)
    interface TracksConsumer{
        fun consume(findTracks: List<Track>)
    }
}