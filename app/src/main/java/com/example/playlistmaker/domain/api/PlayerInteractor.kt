package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

interface PlayerInteractor {
    fun setTrackUrl(url: String)
    fun getTrackUrl(): String
    fun setContext(activity: PlayerActivityModel)
    fun play()
    fun pause()
}