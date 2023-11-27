package com.example.playlistmaker.data.playlistDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.trackInPlaylist.TrackInPlaylistDAO
import com.example.playlistmaker.data.trackInPlaylist.TrackInPlaylistEntity
import com.example.playlistmaker.domain.models.Playlist

@Dao
interface PlaylistDAO {

    @Insert (entity=PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist (playlist: PlaylistEntity)

    @Delete (entity=PlaylistEntity::class)
    fun deletePlaylist (playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun queryPlaylist () : List <PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlistId=:searchId")
    fun findPlaylist (searchId:Int) : PlaylistEntity

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)
}