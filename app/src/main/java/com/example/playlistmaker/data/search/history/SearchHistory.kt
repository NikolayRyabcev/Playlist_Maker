package com.example.playlistmaker.data.search.history

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistory {
    fun addItem(newHistoryTrack: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>
}