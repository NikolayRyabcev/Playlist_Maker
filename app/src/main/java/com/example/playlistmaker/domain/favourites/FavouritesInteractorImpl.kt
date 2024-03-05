package com.example.playlistmaker.domain.favourites

import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesInteractorImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : FavouritesInteractor {

    override fun favouritesAdd(track: Track) {
        favouritesRepository.addTrack(track)
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