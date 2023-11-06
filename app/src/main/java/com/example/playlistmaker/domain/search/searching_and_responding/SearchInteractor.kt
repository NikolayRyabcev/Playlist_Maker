package com.example.playlistmaker.domain.search.searching_and_responding

import com.example.playlistmaker.data.search.requestAndResponse.Resource
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun search (expression:String) : Flow<Resource<List<Track>>>
}