package com.example.playlistmaker.App

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.dagger_di.component.DaggerApplicationComponent


class App : Application() {
    private var appTheme: Boolean = false

    val applicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this, Application())
    }

    override fun onCreate() {
        super.onCreate()


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