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

class PlayerActivity : AppCompatActivity() {

    lateinit var playButton: ImageButton
    lateinit var playerInteractor: PlayerInteractor
    lateinit var pauseButton: ImageButton
    lateinit var progress: TextView
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    lateinit var trackTime: TextView
    lateinit var playerState: PlayerState
    lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        //вью-модель
        val viewModel=ViewModelProvider(this,PlayerViewModel.getViewModelFactory(""))[PlayerViewModel::class.java]
        val playerTrackName = findViewById<TextView>(R.id.playerTrackName)
        val playerArtistName = findViewById<TextView>(R.id.playerArtistName)
        trackTime = findViewById(R.id.time)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val cover = findViewById<ImageView>(R.id.trackCover)
        playButton = findViewById(R.id.playButton)
        playButton.isEnabled = false
        pauseButton = findViewById(R.id.pauseButton)
        progress = findViewById(R.id.trackTimer)
        val arrowButton = findViewById<ImageView>(R.id.playerBackButtonArrow)


        playerInteractor = Creator.providePlayerInteractor()
        playerState = PlayerState.STATE_PAUSED
        mainThreadHandler = Handler(Looper.getMainLooper())
        arrowButton.setOnClickListener {
            finish()
        }
        playerTrackName.text = intent.extras?.getString("Track Name") ?: "Unknown Track"
        playerArtistName.text = intent.extras?.getString("Artist Name") ?: "Unknown Artist"
        trackTime.text = intent.extras?.getString("Track Time") ?: "00:00"
        album.text = intent.extras?.getString("Album") ?: "Unknown Album"
        year.text = (intent.extras?.getString("Year") ?: "Year").take(4)
        genre.text = intent.extras?.getString("Genre") ?: "Unknown Genre"
        country.text = intent.extras?.getString("Country") ?: "Unknown Country"
        val getImage = (intent.extras?.getString("Cover") ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.bfplaceholder)
                .into(cover)
        }
        val url = intent.extras?.getString("URL")

        viewModel.preparePlayer { preparePlayer() }

        if (!url.isNullOrEmpty()) playerInteractor.createPlayer(url) {
            playButton.isEnabled = true
        }

        playButton.setOnClickListener {
            playerInteractor.play()
        }
        pauseButton.setOnClickListener {
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
        playButton.isEnabled = true
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    fun playerStateDrawer() {
        playerState = playerInteractor.playerStateListener()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                playButton.visibility = View.VISIBLE
                playButton.alpha = 0.5f
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PREPARED -> {
                playButton.visibility = View.VISIBLE
                playButton.alpha = 1f
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PAUSED -> {
                playButton.visibility = View.VISIBLE
                playButton.alpha = 1f
                pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PLAYING -> {
                pauseButton.visibility = View.VISIBLE
                playButton.visibility = View.GONE
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
            progress.text = playerInteractor.getTime()
            mainThreadHandler?.postDelayed(updateTimer(), DELAY_MILLIS_Activity)
        }
        return updatedTimer
    }

    companion object {
        const val DELAY_MILLIS_Activity = 100L
    }
}
