package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

class PlayerInteractorImpl : PlayerInteractor {
    var trackAdress = ""

    private lateinit var repository : PlayerRepository
    private lateinit var timeInteractor :TimeInteractor
    override fun setTrackUrl(url: String) {
        trackAdress = url
    }

    override fun getTrackUrl(): String {
        return trackAdress
    }

    override fun play() {
        repository = Creator.providePlayerRepository()
        repository.playing()
    }

    override fun pause() {
        repository.playing()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun setTimerText(time:String) {
        timeInteractor=Creator.provideTimeInteractor()
        timeInteractor.onTimeChanged()
    }
    enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }
}


