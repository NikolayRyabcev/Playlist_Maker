package com.example.playlistmaker.data.dto

data class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()
