package com.example.playlistmaker.di

import androidx.room.Database
import androidx.room.Room
import com.example.playlistmaker.App.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single { Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_database").build() }
    single { get<AppDataBase>().favouritesDao() }
}