package com.example.playlistmaker.domain.api

import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun timing(): Runnable
    fun setPlayerStateListener(listener: PlayerActivity)
}