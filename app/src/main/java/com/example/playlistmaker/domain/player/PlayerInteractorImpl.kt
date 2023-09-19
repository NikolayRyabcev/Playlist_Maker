package com.example.playlistmaker.domain.player

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

    override fun getTime(): String {
        return repository.timeTransfer()
    }
    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}


