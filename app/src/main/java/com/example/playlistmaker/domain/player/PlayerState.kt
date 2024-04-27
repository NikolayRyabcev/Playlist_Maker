package com.example.playlistmaker.domain.player

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    data class Playing (val position: String) : PlayerState()
    data class Paused (val position: String): PlayerState()
}
