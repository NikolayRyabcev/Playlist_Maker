package com.example.playlistmaker.data.search.requestAndResponse

interface NetworkClient {
    suspend fun doRequest (dto:Any) : Response
}