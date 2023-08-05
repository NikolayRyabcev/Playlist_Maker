package com.example.playlistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.UI.search.activity.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.domain.search.models.Track

const val THEME_KEY = "theme"

class App : Application() {
    var theme = true
    override fun onCreate() {
        super.onCreate()
        instance = this
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        Creator.init(this)

        //выставляем тему экрана
        val themeSharedPrefs = getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        if (themeSharedPrefs.contains(THEME_KEY)) {
            theme = themeSharedPrefs.getBoolean(THEME_KEY, true)
        } else {
            theme = isDarkThemeEnabled()
        }
        makeTheme(theme)
    }

    fun isDarkThemeEnabled(): Boolean {
        val applicationContext = instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Log.d("darkmode", "night1")
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //Log.d("darkmode", "night2")
        }
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