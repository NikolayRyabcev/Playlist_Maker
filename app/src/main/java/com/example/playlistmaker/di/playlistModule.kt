package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.App.PlayistDataBase
import com.example.playlistmaker.data.playlistDataBase.PlaylistConverter
import com.example.playlistmaker.data.playlistDataBase.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.domain.playlist.PlaylistRepository
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.NewPlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {
    single {
        Room.databaseBuilder(androidContext(), PlayistDataBase::class.java, "favourites_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { PlaylistConverter() }

    single <PlaylistRepository> {
        PlaylistRepositoryImpl(get(),get(),get())
    }

    single <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }
}