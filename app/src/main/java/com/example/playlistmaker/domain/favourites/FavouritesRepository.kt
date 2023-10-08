package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun addTrack (id:Long)
    fun deleteTrack (id:Long)
    fun getFavourites(id:Long):Flow<List<Track>>
    fun checkFavourites(id:Long):Boolean
}