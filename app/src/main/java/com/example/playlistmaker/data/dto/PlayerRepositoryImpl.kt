package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TimeInteractor
import java.text.SimpleDateFormat

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    var timePlayed = ""
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())

    var timeInteractor: TimeInteractor = Creator.provideTimeInteractor()

    override fun preparePlayer(url: String, completion: () -> Unit) {
        if (playerState != PlayerState.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            completion()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun play() {
        mediaPlayer.start()
        Log.d("PlayerRepositoryImpl", mediaPlayer.isPlaying.toString())
        playerState = PlayerState.STATE_PLAYING
        mainThreadHandler?.post(
            timing()
        )
    }

    override fun pause() {
        mediaPlayer.pause()
        Log.d("PlayerRepositoryImpl", mediaPlayer.isPlaying.toString())
        playerState = PlayerState.STATE_PAUSED
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                    val sdf = SimpleDateFormat("mm:ss")
                    timePlayed = sdf.format(mediaPlayer.currentPosition)
                    Log.d("Плеер", "Время в репозитории $timePlayed")
                    timeInteractor.setTime(timePlayed)
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                }
            }
        }
    }
    companion object {
        const val DELAY_MILLIS = 100L
    }
}