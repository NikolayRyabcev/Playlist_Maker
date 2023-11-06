package com.example.playlistmaker.App

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.playlistDataBase.PlaylistDAO
import com.example.playlistmaker.data.playlistDataBase.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlayistDataBase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO
}