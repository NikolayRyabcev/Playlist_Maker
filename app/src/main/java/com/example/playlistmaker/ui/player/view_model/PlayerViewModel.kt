package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourites.FavouritesInteractor
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerStateListener
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {
    var timeJob: Job? = null
    var stateLiveData = MutableLiveData<PlayerState>()
    var timer = MutableLiveData("00:00")
    val favouritesIndicator = MutableLiveData<Boolean>()
    var favouritesJob:Job?=null

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url, listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)
            }
        })
    }

    fun play() {
        playerInteractor.play()
        timeJob!!.start()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun destroy() {
        timeJob?.cancel()
        playerInteractor.destroy()
    }

    fun getTimeFromInteractor(): LiveData<String> {
        timeJob = viewModelScope.launch {
            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                playerInteractor.getTime().collect() {
                    timer.postValue(it)
                }
            }
        }
        return timer
    }

    fun putTime(): LiveData<String> {
        getTimeFromInteractor()
        timer.value?.let { Log.d("время в модели", it) }
        return timer
    }

    fun onFavoriteClicked(track: Track) {
        Log.d("PlayerViewModel", "onFavoriteClicked")
        Log.d("PlayerViewModel", "$track")
        if (track.isFavorite) {
            track.trackId?.let { favouritesInteractor.favouritesDelete(track) }
        } else track.trackId?.let {
            favouritesInteractor.favouritesAdd(
                track
            )
        }
    }

    fun favouritesChecker (track: Track) : LiveData<Boolean> {

        favouritesJob=viewModelScope.launch{

            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                track.trackId?.let { id ->
                    favouritesInteractor.favouritesCheck(id)
                        .collect {value ->
                            Log.d("Hueta", "$value")
                            favouritesIndicator.postValue(value)
                        }
                }
            }
        }
        return favouritesIndicator
    }


    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}