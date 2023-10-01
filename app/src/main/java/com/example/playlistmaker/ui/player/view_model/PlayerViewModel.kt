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
    var timer = MutableLiveData("00:00")

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url, listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)
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

    fun getTimeFromInteractor() :LiveData<String>{
        viewModelScope.launch {
            delay(PLAYER_BUTTON_PRESSING_DELAY)
            playerInteractor.getTime().collect() {
                timer.postValue(it)
            }
            timer.value?.let { Log.d("время из интерактора", it) }
        }
        return timer
    }

    fun putTime():LiveData<String> {
        getTimeFromInteractor()
        timer.value?.let { Log.d("время в модели", it) }
        return timer
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}