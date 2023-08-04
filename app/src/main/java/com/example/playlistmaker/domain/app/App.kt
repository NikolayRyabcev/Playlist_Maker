package com.example.playlistmaker.domain.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.UI.search.activity.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.domain.search.models.Track

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences(): SharedPreferences {
            return savedHistory
        }

        var trackHistoryList = ArrayList<Track>()
        lateinit var instance: App
            private set
    }
}