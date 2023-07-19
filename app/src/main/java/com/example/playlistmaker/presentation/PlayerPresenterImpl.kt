package com.example.playlistmaker.presentation

import android.view.View

class PlayerPresenterImpl: PlayerPresenter {
    override fun onPlayButton (){
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    override fun onPauseButton (){
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
    }
    override fun enablePlayButton(){
        playButton.isEnabled = true
    }
    override fun setTimerText(time:String){
        timer.text = time
    }
}