package com.example.playlistmaker.presentation.ActivityModels

import com.example.playlistmaker.domain.api.PlayerInteractor

interface PlayerActivityModel {

    fun preparePlayer(url: String)
    fun enablePlayButton()
    fun onPlayButton()
    fun onPauseButton()
    fun setTimerText(time:String)
}