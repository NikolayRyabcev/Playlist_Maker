package com.example.playlistmaker.data.search.request_and_response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient (private val iTunesService:iTunesSearchAPI) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body()?: Response()
            body.apply{resultCode=resp.code()}
        } else {
            Response().apply{resultCode=400}
        }
    }
}