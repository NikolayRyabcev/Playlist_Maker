package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(private val favouritesRepository : FavouritesRepository) : FavouritesInteractor{
    override fun favouritesAdd(id: Long) {
        favouritesRepository.addTrack(id)
    }

    override fun favouritesDelete(id: Long) {
        favouritesRepository.deleteTrack(id)
    }

    override fun favouritesGet(id: Long): Flow<List<Track>> {
        return favouritesRepository.getFavourites(id)
    }

    override fun favouritesCheck(id: Long): Flow<Boolean> {
        return favouritesRepository.checkFavourites(id)
    }
}