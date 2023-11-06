package com.example.playlistmaker.data.favouritesDataBase

import com.example.playlistmaker.domain.models.Track

class TrackConverter {
    fun mapTrackToFavourite(track: Track): FavouritesEntity {
        return FavouritesEntity(
            track.trackId,
            track.addTime,
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
        return Track(
            track.trackName,
            track.addTime,
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