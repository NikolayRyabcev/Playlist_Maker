package com.example.playlistmaker.data.player

import android.annotation.SuppressLint
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    private var timePlayed =  MutableStateFlow("00:00")
    var time : StateFlow<String> = timePlayed.asStateFlow()
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
            //timing()
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
        Log.d("playerStateRep", playerState.toString())
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        listener.onStateChanged(playerState)

        Log.d("playerStateRep", playerState.toString())
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        listener.onStateChanged(playerState)
        playerJob?.cancel()
        Log.d("playerStateRep", playerState.toString())
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun timing() : StateFlow<String>{
        if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
            val sdf = SimpleDateFormat("mm:ss")
            timePlayed.value = sdf.format(mediaPlayer.currentPosition)
        } else {
            timePlayed.value = "00:00"
        }
        timePlayed.emit(timePlayed.value)
        Log.d("время в репозитории", time.value)
        return time
    }


    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }
}