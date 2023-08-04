package com.example.playlistmaker.Creator

import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.settings.ThemeSettings
import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideSettingsIneractor(): SettingsInteractor {
        return SettingsInteractorImpl()
    }

    fun provideThemeSettings(): ThemeSettingsImpl {
        return ThemeSettingsImpl()
    }

    fun provideSharingIneractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl()
    }
}