package com.example.playlistmaker.presentation.ui.Activities

import android.media.MediaPlayer
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
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import java.text.SimpleDateFormat

class PlayerActivity : AppCompatActivity(), PlayerStateListener {
    private var playerState = PlayerStates.STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()

    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton
    lateinit var timer: TextView

    private var mainThreadHandler: Handler? = null
    var time = ""
    private lateinit var interactor: PlayerInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        val playerTrackName = findViewById<TextView>(R.id.playerTrackName)
        val playerArtistName = findViewById<TextView>(R.id.playerArtistName)
        val trackTime = findViewById<TextView>(R.id.time)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val cover = findViewById<ImageView>(R.id.trackCover)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        timer = findViewById(R.id.trackTimer)
        val arrowButton = findViewById<ImageView>(R.id.playerBackButtonArrow)

        mainThreadHandler = Handler(Looper.getMainLooper())
        playButton.setOnClickListener { playbackControl() }
        pauseButton.setOnClickListener { playbackControl() }

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
        if (!url.isNullOrEmpty()) preparePlayer(url)
        val playerInteractor = Creator.providePlayerInteractor()
        playerInteractor.setPlayerStateListener(this)

    }
 /*   private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = PlayerStates.STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStates.STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStates.STATE_PLAYING
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        mainThreadHandler?.post(
            timing()
        )
    }
    private fun pausePlayer() {
        //super.onPause()
        mediaPlayer.pause()
        playerState = PlayerStates.STATE_PAUSED
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }
    private fun playbackControl() {
        when (playerState) {
            PlayerStates.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStates.STATE_PREPARED, PlayerStates.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        playerState = PlayerStates.STATE_PAUSED
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }
    private fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerStates.STATE_PLAYING) {
                    val sdf = SimpleDateFormat("mm:ss")
                    time = sdf.format(mediaPlayer.currentPosition)
                    Log.d("playertime", time)
                    timer.text = time
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                }
                else {timer.text = "00:00"
                    mainThreadHandler?.post(this)
                }
            }
        }
    }*/
    companion object {
        const val DELAY_MILLIS = 100L
    }
    enum class PlayerStates {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    override fun onPlayerStateChanged(playerState: PlayerStates) {
        TODO("Not yet implemented")
    }
}