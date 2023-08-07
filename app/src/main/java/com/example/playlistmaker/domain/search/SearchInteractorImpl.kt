package com.example.playlistmaker.domain.search

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {

    override fun search(expression: String, consumer: SearchInteractor.TracksConsumer) {
        val t=Thread{
            consumer.consume (repository.searchTracks(expression))
        }
        t.start()
    }
}