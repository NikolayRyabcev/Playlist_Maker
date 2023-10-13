package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    fun favouritesAdd (track:Track)
    fun favouritesDelete (track: Track)
    fun favouritesGet(): Flow<List<Track>>
    fun favouritesCheck(id:Long):Flow<Boolean>
}