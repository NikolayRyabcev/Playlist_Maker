package com.example.playlistmaker.data.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    private lateinit var listener: PlayerStateListener
    private var playerJob: Job? = null

    override fun preparePlayer(url: String, listener: PlayerStateListener) {
        this.listener = listener
        if (playerState != PlayerState.STATE_DEFAULT) return
        listener.onStateChanged(playerState)
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
            playerJob?.start()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
    }


    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        listener.onStateChanged(playerState)
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        listener.onStateChanged(playerState)
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        listener.onStateChanged(playerState)
        playerJob?.cancel()
    }

    @SuppressLint("SimpleDateFormat")
    override fun timing(): Flow<String> = flow {
        val sdf = SimpleDateFormat("mm:ss")
        while (true) {
            if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                emit(sdf.format(mediaPlayer.currentPosition))
            } else {
                emit("00:00")
            }
            delay(DELAY_MILLIS)
        }
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }
}