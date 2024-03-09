package com.example.playlistmaker.dagger_di.module

import com.example.playlistmaker.dagger_di.annotations.ApplicationScope
import com.example.playlistmaker.data.favouritesDataBase.FavouritesRepositoryImpl
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.playlistDataBase.PlaylistRepositoryImpl
import com.example.playlistmaker.data.playlistScreen.PlaylistScreenRepositoryImpl
import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.search.history.SearchHistoryImpl
import com.example.playlistmaker.data.settings.ThemeSettingsImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.favourites.FavouritesInteractor
import com.example.playlistmaker.domain.favourites.FavouritesInteractorImpl
import com.example.playlistmaker.domain.favourites.FavouritesRepository
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.domain.playlist.PlaylistRepository
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.history.SearchHistory
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractorImpl
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

    @ApplicationScope
    @Binds
    fun bindFavouritesRepository(
        favouritesRepositoryImpl: FavouritesRepositoryImpl
    ): FavouritesRepository

    @ApplicationScope
    @Binds
    fun bindFavouritesInteractor(
        favouritesInteractorImpl: FavouritesInteractorImpl
    ): FavouritesInteractor

    @ApplicationScope
    @Binds
    fun bindPlayerRepositoryImpl(
        playerRepositoryImpl: PlayerRepositoryImpl
    ): PlayerRepository

    @ApplicationScope
    @Binds
    fun bindPlayerInteractorImpl(
        playerInteractorImpl: PlayerInteractorImpl
    ): PlayerInteractor

    @ApplicationScope
    @Binds
    fun bindPlaylistRepositoryImpl(
        playlistRepositoryImpl: PlaylistRepositoryImpl
    ): PlaylistRepository

    @ApplicationScope
    @Binds
    fun bindPlaylistInteractorImpl(
        playlistInteractorImpl: PlaylistInteractorImpl
    ): PlaylistInteractor

    @ApplicationScope
    @Binds
    fun bindPlaylistScreenRepositoryImpl(
        playlistScreenRepositoryImpl: PlaylistScreenRepositoryImpl
    ): PlaylistScreenRepository

    @ApplicationScope
    @Binds
    fun bindTracksRepositoryImpl(
        tracksRepositoryImpl: TracksRepositoryImpl
    ): TracksRepository

    @ApplicationScope
    @Binds
    fun bindSearchHistoryImpl(
        favouritesRepositoryImpl: SearchHistoryImpl
    ): SearchHistory

    @ApplicationScope
    @Binds
    fun bindSearchHistoryInteractor(
        searchHistoryInteractorImpl: SearchHistoryInteractorImpl
    ): SearchHistoryInteractor

    @ApplicationScope
    @Binds
    fun bindSearchInteractorImpl(
        searchInteractorImpl: SearchInteractorImpl
    ): SearchInteractor
}