package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel

class PlayerInteractorImpl : PlayerInteractor {
    var trackAdress = ""
    lateinit var playerActivity:PlayerActivityModel
    private lateinit var repository : PlayerRepository
    override fun setTrackUrl(url: String) {
        trackAdress = url
    }

    override fun getTrackUrl(): String {
        return trackAdress
    }
    override fun setContext(activity:PlayerActivityModel) {
        playerActivity = activity
        repository = PlayerRepositoryImpl(playerActivity, this)
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


}


