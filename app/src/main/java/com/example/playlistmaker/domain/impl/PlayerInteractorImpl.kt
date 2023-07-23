package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl : PlayerInteractor {
    var trackAdress = ""

    override fun setTrackUrl(url: String) {
        trackAdress = url
        Log.d("player", "Track set $url")
    }

    override fun getTrackUrl(): String {
        Log.d("player", "Track get $trackAdress")
        return trackAdress

    }
}


