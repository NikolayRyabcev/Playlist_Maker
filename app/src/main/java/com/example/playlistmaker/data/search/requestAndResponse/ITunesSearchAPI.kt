package com.example.playlistmaker.data.search.requestAndResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchAPI {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String): Call<TrackResponse>
}