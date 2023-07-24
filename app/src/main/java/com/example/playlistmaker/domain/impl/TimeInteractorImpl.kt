package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TimeInteractor

class TimeInteractorImpl :TimeInteractor {
    private val listeners = mutableListOf<TimeInteractor>()
    private var timeTransfer : String = ""
    override fun onTimeChanged():String{
        return getTime()
    }
    override fun setTime(time:String){
        timeTransfer=time
    }

    override fun getTime(): String {
        return timeTransfer
    }
    override fun subscribe(listener: TimeInteractor) {
        listeners.add(listener)
        listener.onTimeChanged()
    }
}