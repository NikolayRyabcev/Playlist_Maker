package com.example.playlistmaker.UI.player.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.UI.player.view_model.PlayerViewModel
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.search.models.Track

class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    lateinit var playerState: PlayerState
    lateinit var viewModel: PlayerViewModel
    private lateinit var binding: PlayerActivityBinding
    var url=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding=PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //вью-модель
        viewModel=ViewModelProvider(this,PlayerViewModel.getViewModelFactory(""))[PlayerViewModel::class.java]

        binding.playButton.isEnabled = false

        playerState = PlayerState.STATE_PAUSED
        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.playerBackButtonArrow.setOnClickListener {
            finish()
        }
        val track = intent.getParcelableExtra<Track>("track")

        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.trackTimer.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName?: "Unknown Album"
        binding.year.text = (track?.releaseDate ?: "Year").take(4)
        binding.genre.text = track?.primaryGenreName ?: "Unknown Genre"
        binding.country.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.bfplaceholder)
                .into(binding.trackCover)
        }
        url = track?.previewUrl ?: return


        viewModel.createPlayer(url) {
            preparePlayer()
        }

        binding.playButton.setOnClickListener {
            viewModel.play()
        }
        binding.pauseButton.setOnClickListener {
            viewModel.pause()
        }
        mainThreadHandler?.post(
            updateButton()
        )
        mainThreadHandler?.post(
            updateTimer()
        )

    }

    /*override fun onResume() {
        super.onResume()
        viewModel.createPlayer(url) {
            preparePlayer()
        }
    }*/
    override fun onPause (){
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    fun playerStateDrawer() {
        playerState = viewModel.playerStateListener()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 0.5f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PREPARED -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 1f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PAUSED -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 1f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PLAYING -> {
                binding.pauseButton.visibility = View.VISIBLE
                binding.playButton.visibility = View.GONE
            }
        }
    }

    private fun updateButton(): Runnable {
        val updatedButton = Runnable {
            playerStateDrawer()
            mainThreadHandler?.postDelayed(updateButton(), DELAY_MILLIS_Activity)
        }
        return updatedButton
    }

    private fun updateTimer(): Runnable {
        val updatedTimer = Runnable {
            binding.trackTimer.text = viewModel.getTime()
            mainThreadHandler?.postDelayed(updateTimer(), DELAY_MILLIS_Activity)
        }
        return updatedTimer
    }

    companion object {
        const val DELAY_MILLIS_Activity = 100L
    }
}
