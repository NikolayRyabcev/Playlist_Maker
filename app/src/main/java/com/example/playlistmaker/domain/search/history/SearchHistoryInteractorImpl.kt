package com.example.playlistmaker.domain.search.history

import android.annotation.SuppressLint
import android.util.Log
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.data.search.history.SearchHistory


class SearchHistoryInteractorImpl(private val historyRepository: SearchHistory) :
    SearchHistoryInteractor {

    override fun addItem(item: Track) {
        historyRepository.addItem(item)
        Log.d("История", "добавление через интерактор")
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    @SuppressLint("LongLogTag")
    override fun provideHistory(): List<Track>? {
        Log.d("В интеракторе передана история:", historyRepository.provideHistory().toString())
        return historyRepository.provideHistory()
    }
}