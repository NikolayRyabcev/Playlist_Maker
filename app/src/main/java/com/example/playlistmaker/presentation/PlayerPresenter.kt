package com.example.playlistmaker.presentation

import android.view.View

interface PlayerPresenter {
    fun onPlayButton ()
    fun onPauseButton ()
    fun enablePlayButton()
    fun setTimerText(time:String)
}