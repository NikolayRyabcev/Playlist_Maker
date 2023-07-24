package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.OnTimeChangeListener
import com.example.playlistmaker.domain.api.PlayerRepository

class TimeInteractorImpl (private val timeRepository: PlayerRepository) {
    private val listeners = mutableListOf<OnTimeChangeListener>()
    fun getTime(): String {
        return timeRepository.getTime()
    }
    fun subscribe(listener: OnTimeChangeListener) {
        listeners.add(listener)
        listener.onTimeChanged(getTime())
    }
    private fun notifyListeners() {
        for (listener in listeners) {
            listener.onTimeChanged(getTime())
        }
    }
}