package com.example.playlistmaker.data.settings

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.App.App
import com.example.playlistmaker.domain.settings.ThemeSettings
import javax.inject.Inject

const val THEME_KEY = "theme"

class ThemeSettingsImpl @Inject constructor(
    private val application: Application,
    private var themeSharedPrefs: SharedPreferences
) : ThemeSettings {
    private var appTheme: Boolean = false

    override fun lookAtTheme(): Boolean {
        themeSharedPrefs = application.getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        appTheme = themeSharedPrefs.getBoolean(THEME_KEY, !isDarkThemeEnabled())
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
        return appTheme
    }
}