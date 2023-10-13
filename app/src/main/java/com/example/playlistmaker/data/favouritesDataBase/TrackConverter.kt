package com.example.playlistmaker.data.favouritesDataBase

import android.util.Log
import com.example.playlistmaker.domain.search.models.Track

class TrackConverter {
    fun mapTrackToFavourite(track: Track): FavouritesEntity {
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

    fun mapFavouriteToTrack(track: FavouritesEntity): Track {
        Log.d("TrackConverter", track.toString())
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