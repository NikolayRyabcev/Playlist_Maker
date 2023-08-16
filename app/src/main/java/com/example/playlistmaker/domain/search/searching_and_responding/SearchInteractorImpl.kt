package com.example.playlistmaker.domain.search.searching_and_responding

import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(
        expression: String,
        consumer: SearchInteractor.TracksConsumer
    ){
        var tracksData: List<Track>
        val t = Thread {
            tracksData=repository.searchTracks(expression)
            consumer.consume(tracksData)
        }
        t.start()
    }
}