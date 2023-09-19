package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.PlayerState

interface PlayerRepository {

    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, listener: PlayerStateListener)
    fun timeTransfer() :String
    fun playerStateReporter() : PlayerState
}