package com.example.playlistmaker.domain.settings

import android.util.Log
import com.example.playlistmaker.Creator.Creator

class SettingsInteractorImpl (private var themeSettings: ThemeSettings):SettingsInteractor {
    init {
        themeSettings=Creator.provideThemeSettings()
    }
    var currentTheme = true

    override fun getThemeSettings(): Boolean {
        currentTheme=themeSettings.lookAtTheme ()
        val getting = if (currentTheme) "day" else "night"
        Log.d("Тема", "Interactor get $getting")
        return currentTheme
    }

    override fun updateThemeSettings(): Boolean {
        currentTheme=themeSettings.appThemeSwitch ()
        val getting = if (currentTheme) "day" else "night"
        Log.d("Тема", "Interactor update $getting")
        return currentTheme
    }
}