package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.di.PlayerModule.playerModule
import com.example.playlistmaker.di.SearchModule.dataModule
import com.example.playlistmaker.di.SearchModule.searchInteractorModule
import com.example.playlistmaker.di.SearchModule.searchViewModelModule
import com.example.playlistmaker.di.SearchModule.trackRepositoryModule
import com.example.playlistmaker.di.SettingsSharingModule.settingsSharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    private var appTheme: Boolean = false

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
                settingsSharingModule
            )
        }
        instance = this
        Creator.init(this)

        //выставляем тему экрана
        val settingsInteractor = Creator.provideSettingsIneractor()
        appTheme = settingsInteractor.isAppThemeDark()
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