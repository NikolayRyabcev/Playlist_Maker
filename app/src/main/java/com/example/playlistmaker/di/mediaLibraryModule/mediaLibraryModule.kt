package com.example.playlistmaker.di.mediaLibraryModule

import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModels.MediaLibraryViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mediaLibraryModule = module {
    viewModel { FavouritesViewModel(get(), get()) }
    viewModel { PlaylistViewModel() }
    viewModel { MediaLibraryViewModel() }
}