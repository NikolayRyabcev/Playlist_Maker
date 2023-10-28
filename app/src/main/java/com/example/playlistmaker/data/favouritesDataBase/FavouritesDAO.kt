package com.example.playlistmaker.data.favouritesDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.domain.models.Track

@Dao
interface FavouritesDAO {

    @Insert (entity = FavouritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (track: Track)

    @Delete(entity = FavouritesEntity::class)
    fun deleteTrack (track:FavouritesEntity)

    @Query("SELECT * FROM favourites_table ORDER BY addTime DESC")
    fun queryTrack():List<FavouritesEntity>

    @Query("SELECT * FROM favourites_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long):FavouritesEntity?
}