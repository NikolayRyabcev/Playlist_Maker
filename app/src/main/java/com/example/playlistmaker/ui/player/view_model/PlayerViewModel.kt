package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateListener

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    var playerState: PlayerState = playerInteractor.playerStateListener()
    var stateLiveData =MutableLiveData(playerState)
    fun getStateLiveData() {
       // Log.d ("playerState", playerState.toString())
        stateLiveData.postValue(playerState)
    }
    fun onStateChangedListener (){
        playerInteractor.playerStateListener()
    }
    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url,listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(playerState)
            }

        })
    }

    fun play() {
        playerInteractor.play()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun destroy() {
        playerInteractor.destroy()
    }

    fun getTime(): String {
        return playerInteractor.getTime()
    }
}