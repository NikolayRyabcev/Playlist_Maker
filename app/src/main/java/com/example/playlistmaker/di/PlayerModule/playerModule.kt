package com.example.playlistmaker.di.PlayerModule

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model_for_activity.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule= module {
    single <PlayerRepository>{
        PlayerRepositoryImpl()
    }
    single <PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    viewModel { PlayerViewModel(get(),) }
}