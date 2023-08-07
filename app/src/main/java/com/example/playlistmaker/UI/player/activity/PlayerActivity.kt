package com.example.playlistmaker.UI.player.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    lateinit var playerState: PlayerState
    lateinit var viewModel: PlayerViewModel
    private lateinit var binding: PlayerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.playerTrackName.text = intent.extras?.getString("Track Name") ?: "Unknown Track"
        binding.playerArtistName.text = intent.extras?.getString("Artist Name") ?: "Unknown Artist"
        binding.trackTimer.text = intent.extras?.getString("Track Time") ?: "00:00"
        binding.album.text = intent.extras?.getString("Album") ?: "Unknown Album"
        binding.year.text = (intent.extras?.getString("Year") ?: "Year").take(4)
        binding.genre.text = intent.extras?.getString("Genre") ?: "Unknown Genre"
        binding.country.text = intent.extras?.getString("Country") ?: "Unknown Country"
        val getImage = (intent.extras?.getString("Cover") ?: "Unknown Cover").replace(
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
        val url = intent.extras?.getString("URL")

        viewModel.preparePlayer { preparePlayer() }

        if (!url.isNullOrEmpty()) playerInteractor.createPlayer(url) {
            binding.playButton.isEnabled = true
        }

        binding.playButton.setOnClickListener {
            playerInteractor.play()
        }
        binding.pauseButton.setOnClickListener {
            playerInteractor.pause()
        }
        mainThreadHandler?.post(
            updateButton()
        )
        mainThreadHandler?.post(
            updateTimer()
        )

    }


    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroy()
    }

    fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    fun playerStateDrawer() {
        playerState = playerInteractor.playerStateListener()
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
            binding.trackTimer.text = playerInteractor.getTime()
            mainThreadHandler?.postDelayed(updateTimer(), DELAY_MILLIS_Activity)
        }
        return updatedTimer
    }

    companion object {
        const val DELAY_MILLIS_Activity = 100L
    }
}
