package com.example.playlistmaker.data.playlistDataBase

import com.example.playlistmaker.domain.models.Playlist

class PlaylistConverter() {
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
        return PlaylistEntity(
            item.playlistName,
            item.description,
            item.uri,
            item.trackArray,
            item.arrayNumber
        )
    }
}