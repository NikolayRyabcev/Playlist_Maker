package com.example.playlistmaker.di

import androidx.room.Database
import androidx.room.Room
import com.example.playlistmaker.App.AppDataBase
import com.example.playlistmaker.data.favouritesDataBase.FavouritesRepositoryImpl
import com.example.playlistmaker.data.favouritesDataBase.TrackConverter
import com.example.playlistmaker.domain.favourites.FavouritesInteractor
import com.example.playlistmaker.domain.favourites.FavouritesInteractorImpl
import com.example.playlistmaker.domain.favourites.FavouritesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_database").build()
    }

    factory { TrackConverter() }

    single<FavouritesRepository>
    { FavouritesRepositoryImpl(get(), get()) }

    single <FavouritesInteractor> { FavouritesInteractorImpl(get()) }
}