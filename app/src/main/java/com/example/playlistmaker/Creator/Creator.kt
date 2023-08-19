package com.example.playlistmaker.Creator

import com.example.playlistmaker.App.App
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.data.search.request_and_response.RetrofitNetworkClient
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.domain.search.history.SearchHistory
import com.example.playlistmaker.data.search.history.SearchHistoryImpl
import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractorImpl
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractorImpl

object Creator {
/*
    private lateinit var application: App
    fun init(application: App) {
        this.application = application
    }

 /*   //player
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }*/
/*
    //search
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideTracksRepository())
    }

    fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchHistory(): SearchHistory {
        return SearchHistoryImpl(application)
    }

    fun provideSearchHistoryInteractor (): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistory())
    }
*/

    //settings
    fun provideSettingsIneractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideThemeSettings())
    }

    fun provideThemeSettings(): ThemeSettings {
        return ThemeSettingsImpl(application)
    }

    //sharing
    fun provideSharingIneractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }
*/


}