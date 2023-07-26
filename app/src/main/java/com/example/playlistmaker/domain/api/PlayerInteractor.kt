package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: ()->Unit)
    fun getTime(): String
}