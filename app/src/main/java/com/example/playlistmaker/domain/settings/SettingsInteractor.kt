package com.example.playlistmaker.domain.settings

interface SettingsInteractor {
    fun isAppThemeDark ():Boolean

    fun changeThemeSettings(): Boolean
}