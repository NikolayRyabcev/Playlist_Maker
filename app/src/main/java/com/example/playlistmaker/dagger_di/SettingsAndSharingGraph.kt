package com.example.playlistmaker.dagger_di

import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractorImpl
import dagger.Component

@Component
interface SettingsAndSharingGraph {

    fun externalNavigator():ExternalNavigatorImpl

    fun themeSettings(): ThemeSettingsImpl

    fun settingsInteractor(): SettingsInteractorImpl

    fun sharingInteractor(): SharingInteractorImpl
}