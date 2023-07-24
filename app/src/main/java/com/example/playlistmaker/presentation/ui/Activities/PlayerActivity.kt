package com.example.playlistmaker.presentation.ui.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator
import com.example.playlistmaker.Creator.provideTimeInteractor
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.presentation.ActivityModels.PlayerActivityModel

class PlayerActivity : AppCompatActivity(),
    PlayerActivityModel {

    lateinit var playButton: ImageButton
    lateinit var playerInteractor: PlayerInteractor
    lateinit var pauseButton: ImageButton
    lateinit var timer: TextView
    private var mainThreadHandler: Handler? = null
    lateinit var playerState :PlayerInteractorImpl.PlayerState
    private lateinit var timeInteractor: TimeInteractor
    lateinit var trackTime:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        val playerTrackName = findViewById<TextView>(R.id.playerTrackName)
        val playerArtistName = findViewById<TextView>(R.id.playerArtistName)
        trackTime = findViewById(R.id.time)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val cover = findViewById<ImageView>(R.id.trackCover)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        timer = findViewById(R.id.trackTimer)
        val arrowButton = findViewById<ImageView>(R.id.playerBackButtonArrow)
        playerState = PlayerInteractorImpl.PlayerState.STATE_DEFAULT
        playerInteractor = Creator.providePlayerInteractor()


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
        if (!url.isNullOrEmpty()) playerInteractor.setTrackUrl(url)

        playButton.setOnClickListener {
            playerInteractor.play()
            Log.d("player", "Click")
        }
        pauseButton.setOnClickListener {
            playerInteractor.pause()
        }

        when (playerState) {
            PlayerInteractorImpl.PlayerState.STATE_PLAYING -> {
                onPlayButton()
            }

            PlayerInteractorImpl.PlayerState.STATE_PREPARED, PlayerInteractorImpl.PlayerState.STATE_PAUSED -> {
                onPauseButton()
            }

            else -> {preparePlayer()}
        }
        val timeRepository = Creator.providePlayerRepository()
        timeInteractor = provideTimeInteractor()
        timeInteractor.subscribe(provideTimeInteractor())

        setTimerText(timeInteractor.onTimeChanged())
    }


    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroy()
    }

    override fun preparePlayer() {
        playButton.isEnabled = true
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    override fun enablePlayButton() {
        playButton.isEnabled = true
    }

    override fun onPlayButton() {
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
    }

    override fun onPauseButton() {
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    override fun setTimerText(time: String) {
        timer.text = time
    }




}
