package com.example.playlistmaker.domain.search.history

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistory {
    fun addItem(newHistoryTrack: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>
}