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
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    init{
        Log.d("startsettings", "viewModel built")
    }
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

    private val themeLiveData = MutableLiveData(isDarkThemeEnabled())
    fun getthemeLiveData(): LiveData<Boolean> = themeLiveData



    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
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