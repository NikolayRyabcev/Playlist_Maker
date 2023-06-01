package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {
    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences():SharedPreferences { return savedHistory}
    }
    override fun onCreate() {
        super.onCreate()
        savedHistory = applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }
}