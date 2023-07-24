package com.example.playlistmaker.domain.api

interface TimeInteractor {

    fun getTime(): String
    fun subscribe(listener: TimeInteractor)
}