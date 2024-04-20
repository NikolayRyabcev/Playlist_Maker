package com.example.playlistmaker.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default)
    private val playerServiceState = _playerState.asStateFlow()

    private var songUrl = ""

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(200L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return
        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = PlayerState.Prepared

        }
        mediaPlayer?.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerServiceState
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = PlayerState.Default
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    inner class MusicServiceBinder : Binder() {
        fun getMusicService(): MusicService = this@MusicService
    }
}