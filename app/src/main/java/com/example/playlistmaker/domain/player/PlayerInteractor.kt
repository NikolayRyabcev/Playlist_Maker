package com.example.playlistmaker.domain.player

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: ()->Unit)
    fun getTime(): String
    fun playerStateListener(): PlayerState
}