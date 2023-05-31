package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.TrackAdapter.Companion.trackHistoryList

class SearchHistory {


    fun saveHistory(savedHistory: SharedPreferences) {
        for (i in 1..9) {
            savedHistory.edit()
                .putLong(SEARCH_SHARED_PREFS_KEY, trackHistoryList[i])
                .apply()
        }
    }

    fun editArray(historyTrackId: Long) {
        if (trackHistoryList.size < 10) trackHistoryList.add(0, historyTrackId) else {
            trackHistoryList.removeAt(9)
            trackHistoryList.add(0, historyTrackId)
        }
    }
}