package com.example.playlistmaker.domain.player

import com.example.playlistmaker.Creator.Creator

class PlayerInteractorImpl : PlayerInteractor {
    var repository= Creator.providePlayerRepository()


    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun createPlayer(url: String, completion: ()->Unit) {
        repository.preparePlayer(url, completion)
    }

    override fun getTime(): String {
        return repository.timeTransfer()
    }
    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}


