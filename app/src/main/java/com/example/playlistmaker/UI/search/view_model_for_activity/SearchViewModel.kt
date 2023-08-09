package com.example.playlistmaker.UI.search.view_model_for_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.UI.search.view_model_for_activity.screen_states.SearchScreenState
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor

class SearchViewModel(
    private var searchInteractor: SearchInteractor,
    private var searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch())

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }
    private val tracksConsumer = object : SearchInteractor.TracksConsumer {
        override fun consume(tracks: LiveData<List<Track>>) {
            tracks.observeForever { receivedTracks ->
                trackResultList= receivedTracks as MutableLiveData<List<Track>>
            }
        }
    }

    fun onFocusSearcher() {}
    private var trackResultList :MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()

    fun searchResults(searchExpression:String):LiveData<List<Track>> {
        trackResultList= searchInteractor.search(searchExpression, tracksConsumer)
        return trackResultList
    }
    fun searchHistory() {}


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.provideSearchInteractor(),
                        Creator.provideSearchHistoryInteractor(),
                    ) as T
                }
            }
    }
}