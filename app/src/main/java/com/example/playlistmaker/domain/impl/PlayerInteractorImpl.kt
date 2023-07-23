package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity
import java.text.SimpleDateFormat

class PlayerInteractorImpl(
    playerRepository: PlayerRepository,
    private val playerActivity: PlayerActivityModel
) : PlayerInteractor {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerStates.STATE_DEFAULT
    private var playerStateListener: PlayerStateListener? = null
    var time = ""
    private var mainThreadHandler: Handler? = null
    val trackAdress = playerRepository.getAudioTrackUrl()
    override fun playing() {
        if (!trackAdress.isNullOrEmpty()) {
            preparePlayer(trackAdress)
            playbackControl()
        }
    }

    override fun preparePlayer(url: String) {
        if (playerState == PlayerStates.STATE_DEFAULT) {
            Log.d("player", "Track $url")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerActivity.enablePlayButton()
                playerState = PlayerStates.STATE_PREPARED
                playerActivity.onPlayButton()
                Log.d("player", "Prepared")
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerStates.STATE_PREPARED
                playerActivity.onPlayButton()
            }
            playbackControl()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStates.STATE_PLAYING
        playerActivity.onPauseButton()
        Log.d("player", "Started")
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStates.STATE_PAUSED
        playerActivity.onPlayButton()
        Log.d("player", "Paused")
    }

    override fun playbackControl() {
        Log.d("player", "PlaybackControlSet")
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                pausePlayer()
                Log.d("player", "Pause")
            }

            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                startPlayer()
                Log.d("player", "Start")
            }
            else -> {}
        }
    }

    override fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerStates.STATE_PLAYING) {
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

    override fun setPlayerStateListener(listener: PlayerActivity) {
        playerStateListener = listener
    }

    private fun notifyPlayerStateChanged(playerState: PlayerStates) {
        playerStateListener?.onPlayerStateChanged(playerState)
    }
    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerStates.STATE_DEFAULT
        playerActivity.enablePlayButton()
    }

    enum class PlayerStates {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    companion object {
        const val DELAY_MILLIS = 100L
    }
}


