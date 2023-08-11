package com.example.playlistmaker.UI.search.view_model_for_activity

import android.annotation.SuppressLint
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
            stateLiveData.postValue(SearchScreenState.ConnectionError)
        }
    }



    //история
    private var trackHistoryList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
        Log.d("История", "add из вью-модели")
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        Log.d("История", "очистка истории")
    }

    @SuppressLint("LongLogTag")
    fun provideHistory(): LiveData<List<Track>> {
        Log.d("История", "показ истории во вью-модели")
        val history = searchHistoryInteractor.provideHistory()
        trackHistoryList.postValue(history)
        Log.d("Во вью-модели принята история:", history.toString())
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        Log.d("Во вью-модели передана история:", trackHistoryList.value.toString())

        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        stateLiveData.postValue(SearchScreenState.SearchWithHistory(trackHistoryList))
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