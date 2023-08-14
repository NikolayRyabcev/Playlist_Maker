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
        return currentTheme
    }

    override fun updateThemeSettings(): Boolean {
        currentTheme=themeSettings.appThemeSwitch ()
        return currentTheme
    }
}