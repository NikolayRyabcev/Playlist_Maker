package com.example.playlistmaker

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    private val gson = Gson()
    private var json = ""
    var counter = 0
    private val searchValue = SearchActivity()
    var historyList = searchValue.trackHistoryList


    fun editArray(newHistoryTrack: Track) {
        if (json.isNotEmpty()) {
            if (historyList.isEmpty()) {
                if (savedHistory.contains(SEARCH_SHARED_PREFS_KEY)) {
                    val type = object : TypeToken<ArrayList<Track>>() {}.type
                    historyList = gson.fromJson(json, type)
                }
            }
        }
        if (historyList.contains(newHistoryTrack)) {
            historyList.remove(newHistoryTrack)
            historyList.add(0, newHistoryTrack)

        } else {
            if (historyList.size < 10) historyList.add(0, newHistoryTrack)
            else {
                historyList.removeAt(9)
                historyList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    private fun saveHistory() {


        json = gson.toJson(historyList)

        savedHistory.edit()
            .putString(SEARCH_SHARED_PREFS_KEY, json)
            .apply()
        counter = historyList.size
    }

    fun toaster(context: Context, text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }
}