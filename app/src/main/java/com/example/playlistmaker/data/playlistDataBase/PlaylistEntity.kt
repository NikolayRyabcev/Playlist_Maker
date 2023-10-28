package com.example.playlistmaker.data.playlistDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.domain.models.Track

@Entity (tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistName:String,
    val description:String?,
    val uri:String,
    val trackArray:List<Track>,
    val arrayNumber:Int = trackArray.size
)