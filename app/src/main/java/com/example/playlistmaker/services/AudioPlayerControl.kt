package com.example.playlistmaker.services

import com.example.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun provideNotificator()
    fun stopNotification()
}