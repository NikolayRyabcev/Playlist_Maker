package com.example.playlistmaker.UI.main.view_model

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App.App
import com.example.playlistmaker.UI.media_library.Activities.MediaLibraryActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity

class MainViewModel(private val application: App) : ViewModel() {

    fun pressSearch() {
        application.startActivity(Intent(application, SearchActivity::class.java))
    }

    fun pressMediaTech() {
        application.startActivity(
            Intent(
                application,
                MediaLibraryActivity::class.java
            )
        )
    }

    fun pressSettings() {application.startActivity(Intent(application, SettingsActivity::class.java))}

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return MainViewModel(
                        app
                    ) as T
                }
            }
    }
}