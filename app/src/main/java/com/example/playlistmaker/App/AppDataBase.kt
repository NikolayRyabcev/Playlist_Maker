package com.example.playlistmaker.App

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favouritesDataBase.FavouritesDAO
import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity
import com.example.playlistmaker.data.playlistDataBase.PlaylistDAO
import com.example.playlistmaker.data.playlistDataBase.PlaylistEntity

@Database(version=1, entities = [FavouritesEntity::class])
abstract class AppDataBase (): RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDAO
}

@Database(version=1, entities = [PlaylistEntity::class])
abstract class PlayistDataBase (): RoomDatabase() {
    abstract fun playlistDao():  PlaylistDAO
}