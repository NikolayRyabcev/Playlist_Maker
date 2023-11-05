package com.example.playlistmaker.data.playlistDataBase

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {
    val gson = Gson()

    fun mapplaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistName,
            item.description,
            item.uri,
            gson.fromJson(item.trackList, object : TypeToken<List<Long>>() {}.type),
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlaylistEntity {
        return PlaylistEntity(
            0,
            item.playlistName,
            item.description,
            item.uri,
            gson.toJson(item.trackArray),
            item.arrayNumber
        )
    }
}