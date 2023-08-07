package com.example.playlistmaker.UI.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.domain.player.PlayerInteractor

class PlayerViewModel (
    private val trackId: String,
    private val playerInteractor: PlayerInteractor,
        ):ViewModel(){

    fun preparePlayer(completion: ()->Unit){
        playerInteractor.createPlayer(trackId, completion)
    }
    fun playerStateDrawer(){}
    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackId,
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}