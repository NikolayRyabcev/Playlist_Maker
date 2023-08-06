package com.example.playlistmaker.data.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import com.example.playlistmaker.App.App

const val THEME_KEY = "theme"

class ThemeSettingsImpl(private val application: App) :ThemeSettings{
    private var appTheme: Boolean = false
    override fun lookAtTheme () :Boolean {
        val themeSharedPrefs: SharedPreferences = application.getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        if (themeSharedPrefs.contains(THEME_KEY)) {
            appTheme = themeSharedPrefs.getBoolean(THEME_KEY, isLightThemeEnabled())
        } else {
            appTheme= !isLightThemeEnabled()
            val editor = themeSharedPrefs.edit()
            editor.putBoolean(THEME_KEY, appTheme)
            editor.apply()
        }
        val getting = if (appTheme) "day" else "night"
        Log.d("Тема", "Repository look $getting")
        return appTheme
    }
    private fun isLightThemeEnabled(): Boolean {
        val applicationContext = App.instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun appThemeSwitch(): Boolean {
        appTheme = !appTheme
        val getting = if (appTheme) "day" else "night"
        Log.d("Тема", "Repository switch $getting")
        return appTheme
    }
}