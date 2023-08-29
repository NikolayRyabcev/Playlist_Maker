package com.example.playlistmaker.di.SearchModule

import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single <SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single <SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
}