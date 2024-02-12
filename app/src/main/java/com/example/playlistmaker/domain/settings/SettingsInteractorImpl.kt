package com.example.playlistmaker.domain.settings

import javax.inject.Inject

class SettingsInteractorImpl @Inject constructor (private var themeSettings: ThemeSettings):SettingsInteractor {

    var isDarkTheme = true

    //получение информации о включении темной темы
    override fun isAppThemeDark(): Boolean {
        isDarkTheme=themeSettings.lookAtTheme ()
        return isDarkTheme
    }

    //функция смены темы:светлая/темная
    override fun changeThemeSettings(): Boolean {
        isDarkTheme=themeSettings.appThemeSwitch ()
        return isDarkTheme
    }
}