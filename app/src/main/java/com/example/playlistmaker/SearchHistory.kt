package com.example.playlistmaker

import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear
import android.widget.Toast

class SearchHistory {
    val savedHistory = App.getSharedPreferences()
    var trackHistoryList = ArrayList<Long>()

    fun editArray(historyTrackId: Long) {

        // trackHistoryList.add(historyTrackId)
        if (trackHistoryList.contains(historyTrackId))
            if (trackHistoryList.size < 10) trackHistoryList.add(historyTrackId)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(historyTrackId)
            }
        saveHistory(trackHistoryList)

    }

    private fun saveHistory(trackHistoryList: ArrayList<Long>) {
        savedHistory.edit()
            .clear()
            .commit()
        for (i in 0 until trackHistoryList.size) {
            savedHistory.edit()
                .putLong(SEARCH_SHARED_PREFS_KEY, trackHistoryList[i])
                .apply()
        }
    }
}