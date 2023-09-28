package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.requestAndResponse.NetworkClient
import com.example.playlistmaker.data.search.requestAndResponse.Resource
import com.example.playlistmaker.data.search.requestAndResponse.TrackResponse
import com.example.playlistmaker.data.search.requestAndResponse.TrackSearchRequest
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {

        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            when (response.resultCode) {
                -1 -> {
                    emit(Resource.Error(ErrorType.CONNECTION_ERROR))
                }

                200 -> {
                    emit(Resource.Success((response as TrackResponse).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis),
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }))
                }

                else -> {
                    emit(Resource.Error(ErrorType.SERVER_ERROR))
                }
            }
        } catch (error: Error) {
            throw Exception(error)
        }
    }
}