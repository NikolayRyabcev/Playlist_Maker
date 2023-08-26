package com.example.playlistmaker.di.SearchModule

import com.example.playlistmaker.ui.search.viewModelForActivity.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module{
    viewModel { SearchViewModel(get(), get()) }
}