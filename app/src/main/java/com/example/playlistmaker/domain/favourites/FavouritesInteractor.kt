package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    fun favouritesAdd (id:Long)
    fun favouritesDelete (id:Long)
    fun favouritesGet(id:Long): Flow<List<Track>>
    fun favouritesCheck(id:Long):Flow<Boolean>
}