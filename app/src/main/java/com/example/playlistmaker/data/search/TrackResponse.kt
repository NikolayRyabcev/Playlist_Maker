package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.Response
import com.example.playlistmaker.domain.search.models.Track

data class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
) : Response()
