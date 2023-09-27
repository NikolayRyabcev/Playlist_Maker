package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    var timePlayed = "00:00"
    private lateinit var listener: PlayerStateListener
    private var playerJob: Job? = null
    private val playerScope = CoroutineScope(Job() + Dispatchers.Main)

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
            Log.d("playerStateRep", playerState.toString())
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }

        //Log.d ("playerStateRep", playerState.toString())
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING

        timing()

        listener.onStateChanged(playerState)
        Log.d("playerStateRep", playerState.toString())
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        listener.onStateChanged(playerState)
        playerJob?.cancel()
        Log.d("playerStateRep", playerState.toString())
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        listener.onStateChanged(playerState)
        Log.d("playerStateRep", playerState.toString())
    }

    private fun timing() {
        playerJob = playerScope.launch {
            if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                delay(DELAY_MILLIS)
                val sdf = SimpleDateFormat("mm:ss")
                timePlayed = sdf.format(mediaPlayer.currentPosition)
            } else {
                timePlayed = "00:00"
            }
        }
    }

    override fun timeTransfer(): String {
        return timePlayed
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }
}