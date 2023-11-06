package com.example.playlistmaker.domain.search.history

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addItem(item: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>?
}