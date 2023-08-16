package com.example.playlistmaker.data.search.request_and_response

interface NetworkClient {
    fun doRequest (dto:Any) : Response
}