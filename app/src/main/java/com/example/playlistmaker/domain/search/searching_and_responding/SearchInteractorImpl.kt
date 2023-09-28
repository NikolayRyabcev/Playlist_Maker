package com.example.playlistmaker.domain.search.searching_and_responding

import com.example.playlistmaker.data.search.requestAndResponse.Resource
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    (result.data)
                }

                is Resource.Error<*> -> {
                    Pair(null, result.message)
                }
            } as Resource<List<Track>>
        }
    }

}