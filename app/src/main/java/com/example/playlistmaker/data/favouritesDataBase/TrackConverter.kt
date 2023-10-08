package com.example.playlistmaker.data.favouritesDataBase

import com.example.playlistmaker.data.search.requestAndResponse.TrackDto

class TrackConverter {
    fun map(track: TrackDto): FavouritesEntity {
        return FavouritesEntity(
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

    fun map(track: FavouritesEntity): TrackDto {
        return TrackDto(
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