package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.domain.api.PlayerRepository

class TimeInteractorImpl (private val timeRepository: PlayerRepository):TimeInteractor {
    private val listeners = mutableListOf<TimeInteractor>()
    override fun onTimeChanged():String{
        return getTime()
    }


    override fun getTime(): String {
        return timeRepository.getTime()
    }
    override fun subscribe(listener: TimeInteractor) {
        listeners.add(listener)
        listener.onTimeChanged()
    }
}