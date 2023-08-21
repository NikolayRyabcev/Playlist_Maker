package com.example.playlistmaker.di.SearchModule

import android.content.Context
import com.example.playlistmaker.data.search.history.SEARCH_SHARED_PREFS_KEY
import com.example.playlistmaker.data.search.request_and_response.NetworkClient
import com.example.playlistmaker.data.search.request_and_response.RetrofitNetworkClient
import com.example.playlistmaker.data.search.request_and_response.iTunesSearchAPI
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<iTunesSearchAPI> {

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesSearchAPI::class.java)

    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }
}
