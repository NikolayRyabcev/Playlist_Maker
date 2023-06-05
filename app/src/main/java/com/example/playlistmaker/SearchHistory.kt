package com.example.playlistmaker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    private var trackHistoryList = ArrayList<Track>()
    private val gson = Gson()
    private var json = ""


    fun editArray(newHistoryTrack: Track) {
        if (!trackHistoryList.isNullOrEmpty()) {
            if (savedHistory.contains(SEARCH_SHARED_PREFS_KEY)) {
                val type = object : TypeToken<ArrayList<Track>>() {}.type
                trackHistoryList = gson.fromJson(json, type)
            }
        }
        if (trackHistoryList.contains(newHistoryTrack)) {
            trackHistoryList.remove(newHistoryTrack)
            trackHistoryList.add(0, newHistoryTrack)

        } else {
            if (trackHistoryList.size < 10) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    private fun saveHistory() {
        var history = ""
        for (i in 0 until trackHistoryList.size) {
            json = gson.toJson(trackHistoryList[i])
            history += json
        }
        savedHistory.edit()
            .putString(SEARCH_SHARED_PREFS_KEY, history)
            .apply()
    }

}