package com.example.playlistmaker.di.SearchModule

import com.example.playlistmaker.data.search.TracksRepositoryImpl
import com.example.playlistmaker.data.search.history.SearchHistoryImpl
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.history.SearchHistory
import org.koin.dsl.module

val trackRepositoryModule = module {
    single <TracksRepository> {
        TracksRepositoryImpl(get())
    }
    single <SearchHistory> {
        SearchHistoryImpl(get(), get())
    }
}