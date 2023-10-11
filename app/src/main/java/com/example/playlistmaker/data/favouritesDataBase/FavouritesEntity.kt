package com.example.playlistmaker.data.favouritesDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Types.INTEGER

@Entity(tableName = "favourites_table")
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryId:Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    @ColumnInfo(name = "trackId", typeAffinity=INTEGER)
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)