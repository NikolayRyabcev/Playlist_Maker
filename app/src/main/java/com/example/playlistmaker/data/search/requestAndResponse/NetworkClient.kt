package com.example.playlistmaker.data.search.requestAndResponse

interface NetworkClient {
    fun doRequest (dto:Any) : Response
}