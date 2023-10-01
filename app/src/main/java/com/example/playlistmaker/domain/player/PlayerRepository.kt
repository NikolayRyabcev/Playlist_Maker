package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.StateFlow

interface PlayerRepository {

    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, listener: PlayerStateListener)
    fun playerStateReporter() : PlayerState
    suspend fun timing() : StateFlow<String>
}