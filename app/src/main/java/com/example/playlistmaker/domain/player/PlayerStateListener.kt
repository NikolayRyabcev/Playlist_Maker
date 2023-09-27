package com.example.playlistmaker.domain.player

interface PlayerStateListener {
    fun onStateChanged (state:PlayerState)
}