package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl(private val favouritesRepository : FavouritesRepository) : FavouritesInteractor{
    override fun favouritesAdd(id: Long) {
        favouritesRepository.addTrack(id)
    }

    override fun favouritesDelete(track: Track) {
        favouritesRepository.deleteTrack(track)
    }

    override fun favouritesGet(): Flow<List<Track>> {
        return favouritesRepository.getFavourites()
    }

    override fun favouritesCheck(id: Long): Flow<Boolean> {
        return favouritesRepository.checkFavourites(id)
    }
}