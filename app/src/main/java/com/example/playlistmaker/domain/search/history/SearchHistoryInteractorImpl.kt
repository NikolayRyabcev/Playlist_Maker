package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.data.search.history.SearchHistory


class SearchHistoryInteractorImpl(private val historyRepository: SearchHistory) :
    SearchHistoryInteractor {

    override fun addItem(item: Track) {
        historyRepository.addItem(item)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun provideHistory(): List<Track>? {
        return historyRepository.provideHistory()
    }
}