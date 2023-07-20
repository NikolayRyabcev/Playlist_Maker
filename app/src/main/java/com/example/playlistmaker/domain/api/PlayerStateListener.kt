package com.example.playlistmaker.domain.api

import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

interface PlayerStateListener {
    fun onPlayerStateChanged(playerState: PlayerActivity.PlayerStates)
}