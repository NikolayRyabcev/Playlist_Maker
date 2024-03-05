package com.example.playlistmaker.ui.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import com.example.playlistmaker.ui.search.viewModel.screen_states.SearchScreenState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private var searchInteractor: SearchInteractor,
    private var searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    //поиск трека
    fun searchRequesting(searchExpression: String) {
        if (searchExpression.isNotEmpty()) {
            stateLiveData.postValue(SearchScreenState.Loading)
            viewModelScope.launch {
                try {
                    searchInteractor.search(searchExpression)
                        .collect {
                            when (it.message) {
                                ErrorType.CONNECTION_ERROR -> stateLiveData.postValue(
                                    SearchScreenState.ConnectionError
                                )

                                ErrorType.SERVER_ERROR -> stateLiveData.postValue(SearchScreenState.NothingFound)

                                else -> {
                                    trackResultList.postValue(it.data)
                                    stateLiveData.postValue(
                                        if (it.data.isNullOrEmpty())
                                            SearchScreenState.NothingFound
                                        else SearchScreenState.SearchIsOk(it.data)
                                    )
                                }
                            }
                        }
                } catch (error: Error) {
                    stateLiveData.postValue(SearchScreenState.ConnectionError)
                }
            }
        }
    }

    private var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()


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