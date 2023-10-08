package com.example.playlistmaker.App

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favouritesDataBase.FavouritesDAO
import com.example.playlistmaker.data.favouritesDataBase.FavouritesEntity

@Database(version=1, entities = [FavouritesEntity::class])
abstract class AppDataBase (): RoomDatabase() {
    abstract fun favouritesDao():FavouritesDAO
}