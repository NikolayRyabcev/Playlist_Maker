package com.example.playlistmaker.data.favouritesDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.domain.search.models.Track

@Dao
interface FavouritesDAO {

    @Insert (entity = FavouritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (track:Track)

    @Delete(entity = FavouritesEntity::class)
    fun deleteTrack (track:FavouritesEntity)

    @Query("SELECT * FROM favourites_table")
    fun queryTrack():FavouritesEntity

    @Query("SELECT * FROM favourites_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long):FavouritesEntity
}