package com.example.playlistmaker.domain.search.searching_and_responding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.search.models.Track

interface SearchInteractor {
    fun search (expression:String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(findTracks: List<Track>)
    }
}