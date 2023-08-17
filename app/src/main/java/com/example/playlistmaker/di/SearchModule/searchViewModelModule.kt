package com.example.playlistmaker.di.SearchModule

import com.example.playlistmaker.ui.search.view_model_for_activity.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module{
    viewModel { SearchViewModel(get(), get()) }
}