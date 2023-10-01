package com.example.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, listener: PlayerStateListener)
    suspend fun getTime(): Flow<String>
    fun playerStateListener(): PlayerState
}