package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, listener: PlayerStateListener)
    fun timeTransfer() : Flow<String>
    fun playerStateReporter() : PlayerState
}