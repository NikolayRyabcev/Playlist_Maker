package com.example.playlistmaker.domain.search.searching_and_responding

import com.example.playlistmaker.data.search.requestAndResponse.Resource
import com.example.playlistmaker.domain.search.TracksRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun search(
        expression: String,
        consumer: SearchInteractor.TracksConsumer
    ) {
        executor.execute {
            when (val tracksData=repository.searchTracks(expression))  {
                    is Resource.Success -> { consumer.consume(tracksData.data, null) }
                    is Resource.Error -> { consumer.consume(null, tracksData.message) }
                }
        }
    }
}