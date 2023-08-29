package com.example.playlistmaker.data.search.requestAndResponse

data class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()
