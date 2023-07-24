package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private var playerInteractor: PlayerInteractor,
    private var timeInteractor: TimeInteractor
) : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    var timePlayed = ""
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    val trackAdress: String = playerInteractor.getTrackUrl()
    override fun playing() {
        playerInteractor = Creator.providePlayerInteractor()
        timeInteractor = Creator.provideTimeInteractor()
        if (!trackAdress.isNullOrEmpty()) {
            preparePlayer(trackAdress)
            playbackControl()

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
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED

            }

        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING

        Log.d("player", "Started")
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED

        // Log.d("player", "Paused")
    }

    override fun playbackControl() {
        //   Log.d("player", "PlaybackControlSet")
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
                    Log.d("player", timePlayed)
                    timeInteractor.setTime(timePlayed)
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                } else {
                    timeInteractor.setTime("00:00")
                    mainThreadHandler?.post(this)
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
    override fun getPlayerState():PlayerState {
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