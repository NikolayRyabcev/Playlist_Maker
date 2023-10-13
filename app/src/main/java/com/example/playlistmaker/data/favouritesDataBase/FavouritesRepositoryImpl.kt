package com.example.playlistmaker.data.favouritesDataBase

import com.example.playlistmaker.App.AppDataBase
import com.example.playlistmaker.domain.favourites.FavouritesRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl (private val dataBase: AppDataBase, private val converter:TrackConverter): FavouritesRepository {
    override fun addTrack(track:Track) {
        dataBase.favouritesDao().insertTrack(track)
    }

    override fun deleteTrack(track:Track) {
        dataBase.favouritesDao().deleteTrack(converter.map(track))
    }

    override fun getFavourites(): Flow<List<Track>> =flow {
        converter.map(dataBase.favouritesDao().queryTrack())
    }

    override fun checkFavourites(id: Long): Flow<Boolean> = flow {
        !converter.map(dataBase.favouritesDao().queryTrackId(id)).isFavorite
    }
}