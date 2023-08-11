package com.example.playlistmaker.UI.search.view_model_for_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.UI.search.view_model_for_activity.screen_states.SearchScreenState
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import java.lang.Error

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
        override fun consume(tracks: List<Track>) {
                trackResultList.postValue(tracks)
                stateLiveData.postValue(
                    if (tracks.isNullOrEmpty())
                        SearchScreenState.NothingFound
                    else
                        SearchScreenState.SearchIsOk(trackResultList.value!!)
                )
        }
    }

    private var trackResultList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun searchRequesting(searchExpression: String) {
        stateLiveData.postValue(SearchScreenState.Loading)
        try {
           searchInteractor.search(searchExpression, tracksConsumer)
        } catch (error: Error) {
            Log.d("SearchViewModel", "searchRequesting error: $error")
            stateLiveData.postValue(SearchScreenState.ConnectionError)
        }
    }

    fun clearTrackList() {
        trackResultList.value = emptyList<Track>()
    }

    //история
    private var trackHistoryList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun provideHistory(): LiveData<List<Track>> {
        trackHistoryList.postValue(searchHistoryInteractor.provideHistory())
        if (trackHistoryList.value.isNullOrEmpty()) trackHistoryList.postValue(emptyList())
        return trackHistoryList
    }


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