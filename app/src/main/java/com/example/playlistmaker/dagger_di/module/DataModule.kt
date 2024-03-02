package com.example.playlistmaker.dagger_di.module

import com.example.playlistmaker.dagger_di.annotations.ApplicationScope
import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeSettings
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindExternalNavigator(
        externalNavigator: ExternalNavigatorImpl
    ): ExternalNavigator

    @ApplicationScope
    @Binds
    fun bindThemeSettings(
        themeSettingsImpl: ThemeSettingsImpl
    ): ThemeSettings

    @ApplicationScope
    @Binds
    fun bindSharingInteractor(
        sharingInteractorImpl: SharingInteractorImpl
    ): SharingInteractor

    @ApplicationScope
    @Binds
    fun bindSettingsInteractor(
        settingsInteractorImpl: SettingsInteractorImpl
    ): SettingsInteractor
}