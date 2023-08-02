package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.data.settings.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings ():ThemeSettings

    fun updateThemeSettings (settings: ThemeSettings)
}