package com.example.playlistmaker.domain.player

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

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

    override fun getTime(): Flow<String> = flow {
        emit (repository.timing())
    }

    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}


