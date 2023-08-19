package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.PlayerModule.playerModule
import com.example.playlistmaker.di.SearchModule.dataModule
import com.example.playlistmaker.di.SearchModule.searchInteractorModule
import com.example.playlistmaker.di.SearchModule.searchViewModelModule
import com.example.playlistmaker.di.SearchModule.trackRepositoryModule
import com.example.playlistmaker.di.SettingsSharingModule.settingsSharingModule
import com.example.playlistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin


class App : Application(), KoinComponent {
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
                settingsSharingModule,

            )

        }
        val settingsInteractor = getKoin().get<SettingsInteractor>()
        instance = this

        //выставляем тему экрана
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