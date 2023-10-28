package com.example.playlistmaker.data.favouritesDataBase

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.playlistmaker.App.AppDataBase
import com.example.playlistmaker.domain.favourites.FavouritesRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl(
    private val dataBase: AppDataBase,
    private val converter: TrackConverter
) : FavouritesRepository {

    override fun addTrack(track: Track) {
        track.isFavorite = true
        track.addTime = System.currentTimeMillis()
        dataBase.favouritesDao().insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        track.isFavorite = false
        converter.mapTrackToFavourite(track)?.let { dataBase.favouritesDao().deleteTrack(it) }
    }

    override fun getFavourites(): Flow<List<Track>> = flow {
        val favourites = dataBase.favouritesDao().queryTrack()

        Log.d("Избранное репозиторий", favourites.toString())
        if (favourites != null) {
            val favouritesConverted =
                dataBase.favouritesDao().queryTrack().map { converter.mapFavouriteToTrack(it) }
            emit(favouritesConverted)
        } else {
            emit(emptyList())
        }
    }


    override fun checkFavourites(id: Long): Flow<Boolean> = flow {
        emit(dataBase.favouritesDao().queryTrackId(id) != null)
    }
}