package com.example.playlistmaker.data.favouritesDataBase

import androidx.room.PrimaryKey
import com.example.playlistmaker.data.search.requestAndResponse.TrackDto
import com.example.playlistmaker.domain.search.models.Track

class TrackConverter {
    fun map(track: Track): FavouritesEntity {
        return FavouritesEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun map(track: FavouritesEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}