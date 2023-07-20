package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.presentation.ui.Activities.PlayerActivity
import java.text.SimpleDateFormat

class PlayerInteractorImpl(playerRepository: PlayerRepository) : PlayerInteractor {
    private val playerRepository = PlayerRepositoryImpl()
    private val mediaPlayer=MediaPlayer()
    //private val playerActivity = PlayerActivity
    private var playerState = PlayerActivity.PlayerStates.STATE_DEFAULT

    var time = ""
    private var mainThreadHandler: Handler? = null
    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            enablePlayButton()
            playerState = PlayerActivity.PlayerStates.STATE_PREPARED
            onPlayButton ()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerActivity.PlayerStates.STATE_PREPARED
            onPlayButton ()
        }
    }
    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerActivity.PlayerStates.STATE_PLAYING
        onPauseButton ()
        mainThreadHandler?.post(
            timing()
        )
    }
    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerActivity.PlayerStates.STATE_PAUSED
        onPlayButton ()
    }
    override fun playbackControl() {
        when (playerState) {
            PlayerActivity.PlayerStates.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerActivity.PlayerStates.STATE_PREPARED, PlayerActivity.PlayerStates.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }
    override fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerActivity.PlayerStates.STATE_PLAYING) {
                    val sdf = SimpleDateFormat("mm:ss")
                    time = sdf.format(mediaPlayer.currentPosition)
                    Log.d("playertime", time)
                    setTimerText(time)
                    mainThreadHandler?.postDelayed(this, PlayerActivity.DELAY_MILLIS)
                }
                else {setTimerText("00:00")
                    mainThreadHandler?.post(this)
                }
            }
        }
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