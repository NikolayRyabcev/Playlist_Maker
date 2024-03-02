package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.dagger_di.component.DaggerApplicationComponent
import com.example.playlistmaker.di.PlayerModule.playerModule
import com.example.playlistmaker.di.SearchModule.dataModule
import com.example.playlistmaker.di.SearchModule.searchInteractorModule
import com.example.playlistmaker.di.SearchModule.searchViewModelModule
import com.example.playlistmaker.di.SearchModule.trackRepositoryModule
import com.example.playlistmaker.di.favouritesDataModule
import com.example.playlistmaker.di.mediaLibraryModule.mediaLibraryModule
import com.example.playlistmaker.di.playlistModule
import com.example.playlistmaker.di.playlistScreenModule


class App : Application() {
    private var appTheme: Boolean = false

    val applicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this, Application())
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                searchInteractorModule,
                searchViewModelModule,
                trackRepositoryModule,
                playerModule,

                mediaLibraryModule,
                favouritesDataModule,
                playlistModule,
                playlistScreenModule
            )

        }
        instance = this
        makeTheme(appTheme)
    }

    private fun makeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}