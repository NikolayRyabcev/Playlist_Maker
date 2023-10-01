package com.example.playlistmaker.domain.player

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class PlayerInteractorImpl (private val repository:PlayerRepository): PlayerInteractor {



    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun createPlayer(url: String, listener: PlayerStateListener) {
        repository.preparePlayer(url, listener)
    }

    override fun getTime(): Flow<String> {
        Log.d("время в интеракторе", repository.timeTransfer().toString())
        return repository.timeTransfer()
    }
    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}


