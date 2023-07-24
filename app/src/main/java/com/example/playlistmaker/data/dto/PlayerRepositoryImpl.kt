package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.domain.api.OnTimeChangeListener
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private var playerInteractor: PlayerInteractor
) : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerInteractorImpl.PlayerState.STATE_DEFAULT
    var time = ""
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    val trackAdress: String = playerInteractor.getTrackUrl()
    override fun playing() {
        playerInteractor = PlayerInteractorImpl()
        if (!trackAdress.isNullOrEmpty()) {
            preparePlayer(trackAdress)
            playbackControl()

        }
    }

    override fun preparePlayer(url: String) {
        if (playerState == PlayerInteractorImpl.PlayerState.STATE_DEFAULT) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {

                playerState = PlayerInteractorImpl.PlayerState.STATE_PREPARED
                playbackControl()
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerInteractorImpl.PlayerState.STATE_PREPARED

            }

        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerInteractorImpl.PlayerState.STATE_PLAYING

        Log.d("player", "Started")
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerInteractorImpl.PlayerState.STATE_PAUSED

        // Log.d("player", "Paused")
    }

    override fun playbackControl() {
        //   Log.d("player", "PlaybackControlSet")
        when (playerState) {
            PlayerInteractorImpl.PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerInteractorImpl.PlayerState.STATE_PREPARED, PlayerInteractorImpl.PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if ((playerState == PlayerInteractorImpl.PlayerState.STATE_PLAYING) or (playerState == PlayerInteractorImpl.PlayerState.STATE_PAUSED)) {
                    val sdf = SimpleDateFormat("mm:ss")
                    time = sdf.format(mediaPlayer.currentPosition)
                    Log.d("player", time)
                    playerInteractor.setTimerText(time)
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                } else {
                    playerInteractor.setTimerText("00:00")
                    mainThreadHandler?.post(this)
                }
            }
        }
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerInteractorImpl.PlayerState.STATE_DEFAULT
    }

    override fun getTime(): String {
        return time
    }
    override fun subscribe(listener: OnTimeChangeListener) {
        listener.onTimeChanged(time)
    }
    companion object {
        const val DELAY_MILLIS = 100L
    }
}