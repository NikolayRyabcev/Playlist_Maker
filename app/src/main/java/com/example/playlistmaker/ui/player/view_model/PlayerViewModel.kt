package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateListener
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    var stateLiveData = MutableLiveData<PlayerState>()
    var timer = MutableLiveData<String>("")

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url, listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)
            }
        })
        getTimeFromInteractor()
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

    fun getTimeFromInteractor() {
        viewModelScope.launch {
            playerInteractor.getTime().collect() {
                timer.postValue(it)
            }
            timer.value?.let { Log.d("время", it) }
        }
    }

    fun putTime():LiveData<String> {
        return timer
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}