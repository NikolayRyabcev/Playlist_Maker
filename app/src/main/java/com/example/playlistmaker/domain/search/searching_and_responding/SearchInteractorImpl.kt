package com.example.playlistmaker.domain.search.searching_and_responding

import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    val tracksLiveData = MutableLiveData<List<Track>>()
    override fun search(expression: String, consumer: SearchInteractor.TracksConsumer) {
        val t=Thread{
            tracksLiveData.postValue (repository.searchTracks(expression))
        }
        t.start()
        consumer.consume(tracksLiveData)
    }
}