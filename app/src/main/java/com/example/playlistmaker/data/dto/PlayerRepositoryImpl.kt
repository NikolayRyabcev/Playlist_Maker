package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private val playerActivity: PlayerActivityModel,
    playerInteractor: PlayerInteractor
) : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    var time = ""
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    val trackAdress: String = playerInteractor.getTrackUrl()
    override fun playing() {
        if (!trackAdress.isNullOrEmpty()) {
        //    Log.d("player", "Playing")
            preparePlayer(trackAdress)
            playbackControl()

        }
    }

    override fun preparePlayer(url: String) {
        if (playerState == PlayerState.STATE_DEFAULT) {
        //    Log.d("player", "Track $url")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerActivity.enablePlayButton()
                playerState = PlayerState.STATE_PREPARED
          //      Log.d("player", "Prepared")
                playbackControl()
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
                playerActivity.onPauseButton()
            }

        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        playerActivity.onPlayButton()
        Log.d("player", "Started")
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        playerActivity.onPauseButton()
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
                    time = sdf.format(mediaPlayer.currentPosition)
                    Log.d("player", time)
                    playerActivity.setTimerText(time)
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                } else {
                    playerActivity.setTimerText("00:00")
                    mainThreadHandler?.post(this)
                }
            }
        }
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        playerActivity.enablePlayButton()
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