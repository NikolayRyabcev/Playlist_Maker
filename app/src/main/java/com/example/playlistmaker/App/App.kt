package com.example.playlistmaker.App

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
    private var appTheme: Boolean = false


    override fun onCreate() {
        super.onCreate()
        var logger = if (appTheme) "Начальная Светлая" else "Начальная Темная"
        Log.d("Тема", logger)
        instance = this
        savedHistory =
            applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        Creator.init(this)

        //выставляем тему экрана
        val themeSharedPrefs: SharedPreferences = getSharedPreferences(THEME_KEY, MODE_PRIVATE)

        if (themeSharedPrefs.contains(THEME_KEY)) {
            appTheme = themeSharedPrefs.getBoolean(THEME_KEY, isLightThemeEnabled())
            Log.d("Тема", "Тема записана, передаю $logger")
        } else {
            appTheme= !isLightThemeEnabled()
            val editor = themeSharedPrefs.edit()
            editor.putBoolean(THEME_KEY, appTheme)
            editor.apply()
        }
        logger = if (appTheme) "Светлая" else "Темная"
        Log.d("Тема", logger)
        makeTheme(appTheme)
    }

    private fun isLightThemeEnabled(): Boolean {
        val applicationContext = instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }
    fun giveTheme(): Boolean {
        val logger = if (appTheme) "передали: Светлая" else "передали: Темная"
        Log.d("Тема", logger)
        return appTheme
    }

    fun appThemeSwitch() {
        appTheme = !appTheme
        val logger = if (appTheme) "Поменяли, теперь: Светлая" else "Поменяли, теперь: Темная"
        Log.d("Тема", logger)
        makeTheme(appTheme)
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