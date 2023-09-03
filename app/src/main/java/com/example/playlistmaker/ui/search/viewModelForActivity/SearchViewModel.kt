package com.example.playlistmaker.ui.search.viewModelForActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import com.example.playlistmaker.ui.search.viewModelForActivity.screen_states.SearchScreenState

class SearchViewModel(
    private var searchInteractor: SearchInteractor,
    private var searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    //поиск трека
    private val tracksConsumer = object : SearchInteractor.TracksConsumer {
        override fun consume(tracks: List<Track>?, errorMessage: ErrorType?) {
            when (errorMessage) {
                ErrorType.CONNECTION_ERROR -> stateLiveData.postValue(SearchScreenState.ConnectionError)
                ErrorType.SERVER_ERROR -> stateLiveData.postValue(SearchScreenState.NothingFound)

                else -> {
                    trackResultList.postValue(tracks)
                    stateLiveData.postValue(
                        if (tracks.isNullOrEmpty())
                            SearchScreenState.NothingFound
                        else SearchScreenState.SearchIsOk(tracks)
                    )
                }
            }
        }
    }

    private var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()
    fun searchRequesting(searchExpression: String) {
        stateLiveData.postValue(SearchScreenState.Loading)
        try {
            searchInteractor.search(searchExpression, tracksConsumer)
        } catch (error: Error) {
            stateLiveData.postValue(SearchScreenState.ConnectionError)
        }
    }


    //история
    private var trackHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun provideHistory(): LiveData<List<Track>> {
        val history = searchHistoryInteractor.provideHistory()
        trackHistoryList.value = searchHistoryInteractor.provideHistory()
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        trackHistoryList.value = searchHistoryInteractor.provideHistory()
        stateLiveData.value =
            trackHistoryList.value?.let { SearchScreenState.SearchWithHistory(it) }
        Log.d("История", trackHistoryList.value.toString())
    }


}