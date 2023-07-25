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
    var playerState= PlayerRepositoryImpl.PlayerState.STATE_DEFAULT
    override fun setTrackUrl(url: String) {
        Log.d("Плеер", "Принят адрес трека $url")
        trackAdress = url
        repository= Creator.providePlayerRepository(trackAdress)

        Log.d("Плеер", "Сохранен адрес трека $trackAdress")
    }

    override fun getTrackUrl(): String {
        Log.d("Плеер", "Передан адрес трека $trackAdress")
        return trackAdress
    }

    override fun play() {
        repository.playing()
        Log.d("Плеер", "Запустили репозиторий")
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
        return playerState
    }

}


