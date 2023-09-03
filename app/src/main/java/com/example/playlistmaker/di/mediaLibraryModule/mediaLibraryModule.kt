package com.example.playlistmaker.di.mediaLibraryModule

import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mediaLibraryModule = module{
    single {
        viewModel {FavouritesViewModel()}
    }
}