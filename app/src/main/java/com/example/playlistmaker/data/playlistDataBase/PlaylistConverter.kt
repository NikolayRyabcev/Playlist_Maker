package com.example.playlistmaker.data.playlistDataBase

import android.util.Log
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {
    //val gson = Gson()

    fun mapplaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistName,
            item.description,
            item.uri,
            item.trackArray,
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlaylistEntity {
        val tracklist = item.trackArray.toString()
        Log.d("Запись в плейлист", "пишем в конвертер  $tracklist")
        return PlaylistEntity(
            0,
            item.playlistName,
            item.description,
            item.uri,
            item.trackArray,
            item.arrayNumber
        )
    }
}