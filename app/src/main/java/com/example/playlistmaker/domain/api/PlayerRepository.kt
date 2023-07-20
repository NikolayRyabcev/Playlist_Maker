package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface PlayerRepository {
    fun search (expression:String): ArrayList<Track>
}