package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.search.viewModelForActivity.screen_states.SearchScreenState

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    var playerState: PlayerState = playerInteractor.playerStateListener()
    var stateLiveData =MutableLiveData(playerState)
    fun getStateLiveData() {
        Log.d ("playerState", playerState.toString())
        stateLiveData.postValue(playerState)
    }
    fun createPlayer(url: String, completion: () -> Unit) {
        playerInteractor.createPlayer(url, completion)

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