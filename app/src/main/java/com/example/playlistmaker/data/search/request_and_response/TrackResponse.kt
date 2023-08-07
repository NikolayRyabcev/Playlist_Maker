package com.example.playlistmaker.data.search.request_and_response

import com.example.playlistmaker.data.search.request_and_response.Response
import com.example.playlistmaker.data.search.request_and_response.TrackDto

data class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()
