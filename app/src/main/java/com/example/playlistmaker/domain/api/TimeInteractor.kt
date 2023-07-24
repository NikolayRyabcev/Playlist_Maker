package com.example.playlistmaker.domain.api

interface TimeInteractor {
    fun onTimeChanged():String
    fun getTime(): String
    fun subscribe(listener: TimeInteractor)
    fun setTime(time:String)
}