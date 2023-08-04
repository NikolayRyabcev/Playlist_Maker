package com.example.playlistmaker.UI.settings.view_model

import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.app.App
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor,
) : ViewModel() {

    //нажатие на кнопку назад
    private var onBackLiveData = MutableLiveData(false)
    fun onBackClick() {
        onBackLiveData.value = true
    }
    fun getOnBackLiveData(): LiveData<Boolean> = onBackLiveData


    //Сохраняем темную тему в LiveData
    fun isDarkThemeEnabled(): Boolean {
        val applicationContext = App.instance.applicationContext
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    var themeLiveData = MutableLiveData(isDarkThemeEnabled())
    fun getthemeLiveData(): LiveData<Boolean> {
        return themeLiveData
    }

    fun themeSwitch() {
        if (themeLiveData.value == true) {
            themeLiveData.value = false
            Log.d("darkmode", "model false")
        } else {
            themeLiveData.value = true
            Log.d("darkmode", "model true")
        }
    }

    //делимся приложением
    fun shareApp() {
        sharingInteractor = Creator.provideSharingIneractor()
        sharingInteractor.shareApp()

    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    Log.d("startsettings", "viewModel factory worked")
                    return SettingsViewModel(
                        Creator.provideSharingIneractor(),
                        Creator.provideSettingsIneractor()
                    ) as T
                }
            }
    }
}