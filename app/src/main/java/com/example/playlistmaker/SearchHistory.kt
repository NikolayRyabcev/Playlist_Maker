package com.example.playlistmaker

import com.google.gson.Gson

class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    private val trackHistoryList = ArrayList<Track>()
    val gson = Gson()
    lateinit var json:String

    fun editArray(newHistoryTrack: Track) {
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
        saveHistory(newHistoryTrack)
    }
    private fun saveHistory(newHistoryTrack: Track) {
        val gson = Gson()
        for (i in 0 until trackHistoryList.size) {
            json = gson.toJson(trackHistoryList[i])
            savedHistory.edit()
                .putString(SEARCH_SHARED_PREFS_KEY, json)
                .apply()
        }
    }
    private fun openHistory(): ArrayList<Track> {
        var historyArray = ArrayList<Track>()
        //historyArray = gson.fromJson(json,Track::class.java)
        return historyArray
    }

}