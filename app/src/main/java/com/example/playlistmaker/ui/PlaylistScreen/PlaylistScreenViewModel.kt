package com.example.playlistmaker.ui.PlaylistScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistScreenInteractor: PlaylistScreenInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isAppThemeDark()
    }

    val trackList : MutableLiveData <List <Track>> = MutableLiveData(emptyList())
    fun getTrackList (playlist: Playlist) {
        viewModelScope.launch {
            playlistScreenInteractor.getTrackList(playlist).collect {
                list -> trackList.postValue(list)
            }
        }
    }

    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }

    fun deleteTrack (track: Track, playlist: Playlist){
        playlist.trackArray = playlist.trackArray.filter { it != track.trackId }
        playlist.arrayNumber = playlist.arrayNumber?.minus(1)
        playlistInteractor.update(track, playlist)
    }

    val playlistTime: MutableLiveData <String> = MutableLiveData("")
    fun getPlaylistTime (playlist: Playlist) {
        viewModelScope.launch {
            playlistScreenInteractor.timeCounting(playlist).collect{
                readyTime -> playlistTime.postValue(readyTime)
            }
        }
    }
}