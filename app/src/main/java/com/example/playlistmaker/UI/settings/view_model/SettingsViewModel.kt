package com.example.playlistmaker.UI.settings.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.App.App
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor,
    val application: App
) : ViewModel() {
    init{        sharingInteractor = Creator.provideSharingIneractor()}

    //нажатие на кнопку назад
    private var onBackLiveData = MutableLiveData(false)
    fun onBackClick() {
        onBackLiveData.value = true
    }
    fun getOnBackLiveData(): LiveData<Boolean> = onBackLiveData


    //Сохраняем тему в LiveData
    private var themeLiveData = MutableLiveData(application.giveTheme())
    fun getThemeLiveData(): LiveData<Boolean> {
        return themeLiveData
    }

    fun themeSwitch() {
        if (themeLiveData.value == true) {
            themeLiveData.value = false
            application.appThemeSwitch ()
            Log.d("darkmode", "model false")
        } else {
            themeLiveData.value = true
            application.appThemeSwitch ()
            Log.d("darkmode", "model true")
        }
    }

    //делимся приложением
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    //пишем в поддержку
    fun writeSupport() {
        sharingInteractor.openSupport()
    }


    //читаем соглашение
    fun readAgreement (){
        sharingInteractor.openTerms()
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return SettingsViewModel(
                        Creator.provideSharingIneractor(),
                        Creator.provideSettingsIneractor(),
                        app
                    ) as T
                }
            }
    }
}