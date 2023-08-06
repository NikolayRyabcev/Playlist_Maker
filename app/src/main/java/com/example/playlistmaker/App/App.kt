package com.example.playlistmaker.App

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.UI.search.activity.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.data.settings.THEME_KEY
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.settings.SettingsInteractor


class App : Application() {
    private var appTheme: Boolean = false


    override fun onCreate() {
        super.onCreate()

        instance = this
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        Creator.init(this)

        //выставляем тему экрана
        val settingsInteractor = Creator.provideSettingsIneractor()
        appTheme = settingsInteractor.getThemeSettings()
        var logger = if (appTheme) "Начальная Светлая" else "Начальная Темная"
        Log.d("Тема", logger)
        makeTheme(appTheme)
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }




    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getTrackSharedPreferences(): SharedPreferences {
            return savedHistory
        }

        var trackHistoryList = ArrayList<Track>()
        lateinit var instance: App
            private set
    }
}