package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.TimeInteractor

class PlayerInteractorImpl : PlayerInteractor {
    var repository= Creator.providePlayerRepository()
    private lateinit var timeInteractor :TimeInteractor

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

    override fun setTimerText(time:String) {
        timeInteractor=Creator.provideTimeInteractor()
        timeInteractor.onTimeChanged()
    }

}


