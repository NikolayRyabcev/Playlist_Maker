package com.example.playlistmaker.data.dto

import android.content.Context
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(
    context: Context
) : PlayerRepository {

    private val sharedPreferences =
        context.getSharedPreferences("audio_track", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    override fun saveAudioTrackUrl(url: String) {
        editor.putString("audio_track_url", url)
        editor.apply()
    }

    override fun getAudioTrackUrl(): String? {
        return sharedPreferences.getString("audio_track_url", null)
    }

    override fun clearAudioTrackUrl() {
        editor.remove("audio_track_url")
        editor.apply()
    }
}