package com.example.playlistmaker.domain.search.history

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App.App
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.UI.search.activity.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.data.search.history.SearchHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryInteractorImpl(private val historyRepository: SearchHistory) :
    SearchHistoryInteractor {
    var tracksLiveData = MutableLiveData<List<Track>>()
    override fun addItem(item: Track) {
        historyRepository.addItem(item)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun provideHistory(): List<Track>? {
        tracksLiveData.postValue(provideHistory())
        return tracksLiveData.value
    }
}