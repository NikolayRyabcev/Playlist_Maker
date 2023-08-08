package com.example.playlistmaker.domain.search.searching_and_responding

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.search.models.Track

interface SearchInteractor {
    fun search (expression:String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(findTracks: LiveData<List<Track>>)
    }
}