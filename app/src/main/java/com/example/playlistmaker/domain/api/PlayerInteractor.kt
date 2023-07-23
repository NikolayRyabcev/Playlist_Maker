package com.example.playlistmaker.domain.api

import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity

interface PlayerInteractor {
    fun setTrackUrl(url: String)
    fun getTrackUrl(): String

}