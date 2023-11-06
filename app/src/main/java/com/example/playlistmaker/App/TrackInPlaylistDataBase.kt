package com.example.playlistmaker.App

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.playlistDataBase.PlaylistDAO
import com.example.playlistmaker.data.playlistDataBase.PlaylistEntity
import com.example.playlistmaker.data.trackInPlaylist.TrackInPlaylistDAO
import com.example.playlistmaker.data.trackInPlaylist.TrackInPlaylistEntity

@Database(version = 1, entities = [TrackInPlaylistEntity::class])
abstract class TrackInPlaylistDataBase : RoomDatabase() {
    abstract fun trackListingDao(): TrackInPlaylistDAO
}