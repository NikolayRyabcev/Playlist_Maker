package com.example.playlistmaker.data.search.history

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistory {
    fun addItem(item: Track)
    fun clearHistory()
    fun provideHistory(): ArrayList<Track>
}