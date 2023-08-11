package com.example.playlistmaker.domain.search.searching_and_responding

import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(
        expression: String,
        consumer: SearchInteractor.TracksConsumer
    ){
        var tracksData = emptyList<Track>()
        val t = Thread {
            tracksData=repository.searchTracks(expression)
        }
        t.start()
        consumer.consume(tracksData)

    }
}