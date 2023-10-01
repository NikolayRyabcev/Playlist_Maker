package com.example.playlistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: PlayerActivityBinding
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.isEnabled = false


        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.playerBackButtonArrow.setOnClickListener {
            finish()
        }
        val track = intent.getParcelableExtra<Track>("track")

        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.time.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName ?: "Unknown Album"
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

        playerViewModel.createPlayer(url)

        binding.playButton.setOnClickListener {
            if (playerViewModel.stateLiveData.value == PlayerState.STATE_PLAYING) playerViewModel.pause() else playerViewModel.play()
        }
        binding.pauseButton.setOnClickListener {
            playerViewModel.pause()
        }
        mainThreadHandler?.post(
            updateButton()
        )
        updateTimer()

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }

    private fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    fun playerStateDrawer() {

        playerViewModel.stateLiveData.observe(this) {
            when (playerViewModel.stateLiveData.value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 0.5f

                }

                PlayerState.STATE_PREPARED -> {
                    preparePlayer()
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 1f

                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 1f

                }

                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause)

                }

                else -> {

                }
            }
        }
    }

    private fun updateButton(): Runnable {
        val updatedButton = Runnable {
            playerStateDrawer()
            mainThreadHandler?.postDelayed(updateButton(), PLAYER_BUTTON_PRESSING_DELAY)
        }
        return updatedButton
    }

    private fun updateTimer() {
        playerViewModel.putTime().observe(this) {
            binding.trackTimer.text = it
        }
    }


    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}
