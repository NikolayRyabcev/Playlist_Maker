package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryInteractor {
    fun addItem(item: Track)
    fun clearHistory()
    fun provideHistory(): ArrayList<Track>
}