package com.example.playlistmaker.UI.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App.App
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel

class MainViewModel(val application: App) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return MainViewModel(app
                    ) as T
                }
            }
    }
}