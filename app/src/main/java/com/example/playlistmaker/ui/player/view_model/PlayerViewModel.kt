package com.example.playlistmaker.ui.player.view_model

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourites.FavouritesInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.services.AudioPlayerControl
import com.example.playlistmaker.ui.player.buttonView.CustomViewClickListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favouritesInteractor: FavouritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), CustomViewClickListener {

    private val favouritesIndicator = MutableLiveData<Boolean>()
    private var favouritesJob: Job? = null
    val playlistList: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>(emptyList())


    val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl
                .getPlayerState()
                .collect {
                    playerState.value = it
                    if (it is PlayerState.Prepared) audioPlayerControl.stopNotification()

                }
        }
    }

    private fun onPlayerButtonClicked() {
        if (playerState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    override fun onViewClicked() {
        onPlayerButtonClicked()
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            track.trackId?.let { favouritesInteractor.favouritesDelete(track) }
        } else track.trackId?.let {
            favouritesInteractor.favouritesAdd(
                track
            )
        }
    }

    fun favouritesChecker(track: Track): LiveData<Boolean> {
        favouritesJob = viewModelScope.launch {
            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                track.trackId?.let { id ->
                    favouritesInteractor.favouritesCheck(id)
                        .collect { value ->
                            favouritesIndicator.postValue(value)
                        }
                }
            }
        }
        return favouritesIndicator
    }

    fun playlistMaker(): LiveData<List<Playlist>> {
        viewModelScope.launch {
            playlistInteractor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlistList.postValue(it)
                    } else {
                        playlistList.postValue(emptyList())
                    }
                }
        }
        return playlistList
    }

    val playlistAdding = MutableLiveData(false)

    fun addTrack(track: Track, playlist: Playlist) {
        if (playlist.trackArray.contains(track.trackId)) {
            playlistAdding.postValue(true)
        } else {
            playlistAdding.postValue(false)
            playlist.trackArray = (playlist.trackArray + track.trackId)
            playlist.arrayNumber = (playlist.arrayNumber?.plus(1))!!
            playlistInteractor.update(track, playlist)
        }
    }

    fun showNotification() {
        audioPlayerControl?.provideNotificator()
    }

    fun hideNotification() {
        audioPlayerControl?.stopNotification()
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
        const val TAG = "PlayerViewModel"
    }

}