package com.example.playlistmaker.data.playlistScreen

import android.app.Application
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenRepository

class PlaylistScreenRepositoryImpl (
    private val application: Application,
    private val playlist: Playlist
    ):PlaylistScreenRepository {

    override fun sharePlaylist() {
        val trackList : List<Track> = emptyList() //достать треки из плейлиста
        val nameOfPlaylist = playlist.playlistName
        val desriptionOfPlaylist= playlist.description
        val trackNumber = playlist.arrayNumber
        var trackInfo = "$nameOfPlaylist /n $desriptionOfPlaylist /n $trackNumber треков /n"
        trackList.forEach { track ->
            val i=trackList.indexOf(track)
            val name = track.trackName
            val duration = track.trackTimeMillis
            trackInfo = "$trackInfo $i. $name  - ($duration)"
        }

        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, trackInfo)
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }
}