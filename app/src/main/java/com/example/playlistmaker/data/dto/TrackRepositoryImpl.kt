package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl:TrackRepository {
    override fun search(expression: String): ArrayList<Track> {
        TODO("Not yet implemented")
    }
}