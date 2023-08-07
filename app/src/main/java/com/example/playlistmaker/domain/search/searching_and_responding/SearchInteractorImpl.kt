package com.example.playlistmaker.domain.search.searching_and_responding

import com.example.playlistmaker.domain.search.TracksRepository

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {

    override fun search(expression: String, consumer: SearchInteractor.TracksConsumer) {
        val t=Thread{
            consumer.consume (repository.searchTracks(expression))
        }
        t.start()
    }
}