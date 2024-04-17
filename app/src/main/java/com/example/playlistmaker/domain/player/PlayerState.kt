package com.example.playlistmaker.domain.player

sealed class PlayerState {
    class Default : PlayerState()
    class Prepared : PlayerState()
    class Playing (val song:String) : PlayerState()
    class Paused (val song:String): PlayerState()
}
