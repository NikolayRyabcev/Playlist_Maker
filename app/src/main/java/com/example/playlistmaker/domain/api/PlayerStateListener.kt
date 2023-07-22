package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.impl.PlayerInteractorImpl

interface PlayerStateListener {
    fun onPlayerStateChanged(playerState: PlayerInteractorImpl.PlayerStates)
}