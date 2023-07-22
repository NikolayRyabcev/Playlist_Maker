package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface PlayerRepository {
    fun saveAudioTrackUrl(url: String)

    fun getAudioTrackUrl(): String?

    fun clearAudioTrackUrl()
}