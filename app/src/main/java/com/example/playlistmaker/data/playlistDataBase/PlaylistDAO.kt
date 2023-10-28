package com.example.playlistmaker.data.playlistDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.domain.models.Playlist

@Dao
interface PlaylistDAO {

    @Insert (entity=PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist (playlist: Playlist)

    @Delete (entity=PlaylistEntity::class)
    fun deletePlaylist (playlist: Playlist)

    @Query("SELECT * FROM playlist_table")
    fun queryPlaylist () : List <Playlist>
}