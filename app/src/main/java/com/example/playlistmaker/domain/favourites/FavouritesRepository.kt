package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository  {
    fun addTrack (track: Track)
    fun deleteTrack (track: Track)
    fun getFavourites():Flow<List<Track>>
    fun checkFavourites(id:Long):Flow<Boolean>
}