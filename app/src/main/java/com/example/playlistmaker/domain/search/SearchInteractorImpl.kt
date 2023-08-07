package com.example.playlistmaker.domain.search

import java.util.concurrent.Executor

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(expression: String, consumer: SearchInteractor.TracksConsumer) {
        private val executor = Executor.newCachedThreadPool()
    }
}