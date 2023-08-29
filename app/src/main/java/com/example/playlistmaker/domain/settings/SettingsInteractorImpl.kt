package com.example.playlistmaker.domain.settings

class SettingsInteractorImpl (private var themeSettings: ThemeSettings):SettingsInteractor {

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