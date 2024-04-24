package com.example.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
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
    private var trackInfo = ""

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

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        val artist = intent?.getStringExtra("songArtist") ?: ""
        val track = intent?.getStringExtra("songName") ?: ""
        trackInfo = "$artist - $track"
        Log.d("плеер в onBind", trackInfo)
        initMediaPlayer()
        return binder
    }

    //уведомления
    override fun provideNotificator() {
        createNotificationChannel()
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun stopNotification() {
        stopService(Intent(this, MusicService::class.java))
    }

    private fun createNotificationChannel() {
        Log.d("плеер", "createNotificationChannel")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d("плеер", "createNotificationChannelIfBranch")
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText(trackInfo)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
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

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    inner class MusicServiceBinder : Binder() {
        fun getMusicService(): MusicService = this@MusicService
    }

    private companion object {
        const val LOG_TAG = "MusicService"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
    }
}
