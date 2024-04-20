package com.example.playlistmaker.domain.player

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    class Playing (position:String) : PlayerState()
    class Paused (position:String): PlayerState()
}
