package com.example.playlistmaker.data.playlistDataBase

import android.util.Log
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {
    val gson = Gson()

    fun mapplaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.fromJson(item.trackList, object : TypeToken<List<Long>>() {}.type),
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlaylistEntity {
        val tracklist = item.trackArray.toString()
        Log.d("Запись в плейлист", "пишем в конвертер  $tracklist")
        return PlaylistEntity(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.toJson(item.trackArray),
            item.arrayNumber
        )
    }
}