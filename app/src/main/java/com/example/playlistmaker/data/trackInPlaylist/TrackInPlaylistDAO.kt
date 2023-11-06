package com.example.playlistmaker.data.trackInPlaylist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.domain.models.Track

@Dao
interface TrackInPlaylistDAO {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (track: Track)
/*
    @Delete(entity = FavouritesEntity::class)
    fun deleteTrack (track: FavouritesEntity)

    @Query("SELECT * FROM favourites_table ORDER BY addTime DESC")
    fun queryTrack():List<FavouritesEntity>

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long): TrackInPlaylistEntity?*/
}