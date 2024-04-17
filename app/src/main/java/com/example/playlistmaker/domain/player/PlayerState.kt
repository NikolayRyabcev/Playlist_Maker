package com.example.playlistmaker.domain.player

sealed class PlayerState {
    class Default : PlayerState()
    class Prepared : PlayerState()
    class Playing (val position:String) : PlayerState()
    class Paused (val position:String): PlayerState()
}
