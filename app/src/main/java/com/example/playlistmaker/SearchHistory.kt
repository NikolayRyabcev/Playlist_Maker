package com.example.playlistmaker

import com.google.gson.Gson

class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    val trackHistoryList = ArrayList<Track>()
    fun editArray(newHistoryTrack: Track): ArrayList<Track> {

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
        //saveHistory(trackHistoryList)
        return trackHistoryList
    }

    private fun saveHistory(trackHistoryList: ArrayList<Track>) {
        val gson = Gson()

        savedHistory.edit()
            .clear()
            .apply()
        for (i in 0 until trackHistoryList.size) {
            savedHistory.edit()
                .putString(SEARCH_SHARED_PREFS_KEY, trackHistoryList[i])
                .apply()
        }
    }
    private fun openHistory() :ArrayList<Track> {
        val historyArray =ArrayList<Track>()

        return historyArray
    }

}