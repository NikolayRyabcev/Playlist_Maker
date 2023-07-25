package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

class PlayerInteractorImpl : PlayerInteractor {
    var trackAdress = ""

    lateinit var repository :PlayerRepository
    private lateinit var timeInteractor :TimeInteractor
    lateinit var playerState: PlayerRepositoryImpl.PlayerState
    override fun setTrackUrl(url: String) {
        trackAdress = url
        repository= Creator.providePlayerRepository(trackAdress)
        playerState= PlayerRepositoryImpl.PlayerState.STATE_PLAYING

    }

    override fun getTrackUrl(): String {
        return trackAdress
    }

    override fun play() {
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
        Log.d("Плеер", "setTimerText $time")
    }

    override fun setPlayerState() {
        playerState= repository.getPlayerState()
    }

    override fun putPlayerState() :PlayerRepositoryImpl.PlayerState{
        setPlayerState()
        return playerState
    }

}


