package com.example.playlistmaker.data.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import com.example.playlistmaker.App.App
import com.example.playlistmaker.domain.settings.ThemeSettings

const val THEME_KEY = "theme"

class ThemeSettingsImpl(private val application: App) : ThemeSettings {
    private var appTheme: Boolean = false
    private lateinit var themeSharedPrefs: SharedPreferences
    override fun lookAtTheme(): Boolean {
        themeSharedPrefs = application.getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        appTheme = themeSharedPrefs.getBoolean(THEME_KEY, !isDarkThemeEnabled())

        val getting = if (appTheme) "day" else "night"
        Log.d("ТемаСП", "SP contains $getting")

        return appTheme
    }

    private fun isDarkThemeEnabled(): Boolean {
        val applicationContext = App.instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun appThemeSwitch(): Boolean {
        appTheme = !appTheme
        val editor = themeSharedPrefs.edit()
        editor.putBoolean(THEME_KEY, appTheme)
        editor.apply()
        var getting = if (appTheme) "day" else "night"
        Log.d("Тема", "SP put $getting")
        getting = if (appTheme) "day" else "night"
        Log.d("Тема", "Repository switch $getting")
        return appTheme
    }
}