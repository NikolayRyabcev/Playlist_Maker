package com.example.playlistmaker.di

import com.example.playlistmaker.data.playlistScreen.PlaylistScreenRepositoryImpl
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractorImpl
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenRepository
import com.example.playlistmaker.ui.PlaylistScreen.PlaylistScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistScreenModule = module {

    single<PlaylistScreenInteractor> { PlaylistScreenInteractorImpl(get()) }
    single<PlaylistScreenRepository> { PlaylistScreenRepositoryImpl(get()) }
    viewModel { PlaylistScreenViewModel(get(), get()) }
}