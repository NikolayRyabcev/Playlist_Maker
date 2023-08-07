package com.example.playlistmaker.Creator

import com.example.playlistmaker.App.App
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.search.RetrofitNetworkClient
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.SearchInteractorImpl
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractorImpl

object Creator {

    private lateinit var application: App
    fun init(application: App) {
        this.application = application
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun provideSettingsIneractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideThemeSettings())
    }

    fun provideThemeSettings(): ThemeSettings {
        return ThemeSettingsImpl(application)
    }

    fun provideSharingIneractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideTracksRepository())
    }

    fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

}