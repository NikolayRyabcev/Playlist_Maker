package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.PlayerRepositoryImpl

interface PlayerRepository {
    fun playing()
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun timing(): Runnable
    fun destroy()
    fun getTime(): String
    fun subscribe(listener: TimeInteractor)
    fun getPlayerState(): PlayerRepositoryImpl.PlayerState
}