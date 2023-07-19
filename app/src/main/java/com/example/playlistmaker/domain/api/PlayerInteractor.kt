package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun timing(): Runnable
}