package com.example.playlistmaker.domain.api

interface PlayerRepository {
//    fun preparePlayer(url: String)
    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timeTransfer() :String
}