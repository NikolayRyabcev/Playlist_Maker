package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TimeInteractor
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(val trackAdress: String) : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    var timePlayed = ""
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())

    lateinit var timeInteractor: TimeInteractor


    override fun playing() {
        timeInteractor = Creator.provideTimeInteractor()
        Log.d("Плеер", "Адрес трека $trackAdress")
        if (trackAdress.isNotEmpty()) {
            preparePlayer(trackAdress)
            playbackControl()
            Log.d("Плеер", "Репозиторий работает")
        }
    }

    override fun preparePlayer(url: String) {
        if (playerState == PlayerState.STATE_DEFAULT) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {

                playerState = PlayerState.STATE_PREPARED
                playbackControl()
                Log.d("Плеер", "Плеер готов")
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED

            }

        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                    val sdf = SimpleDateFormat("mm:ss")
                    timePlayed = sdf.format(mediaPlayer.currentPosition)
                    Log.d("Плеер", "Время в репозитории $timePlayed")
                    timeInteractor.setTime(timePlayed)
                   // mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                } else {
                    timeInteractor.setTime("00:00")
                 //   mainThreadHandler?.post(this)
                }
            }
        }
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun getTime(): String {
        return timePlayed
    }

    override fun subscribe(listener: TimeInteractor) {
        listener.onTimeChanged()
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    companion object {
        const val DELAY_MILLIS = 100L
    }
}