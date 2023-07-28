package com.example.playlistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.presentation.ui.Activities.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.domain.models.Track

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences(): SharedPreferences {
            return savedHistory
        }

        var trackHistoryList = ArrayList<Track>()
    }
}