package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {
    companion object {
        private val STATE_DEFAULT = 0
        private val STATE_PREPARED = 1
        private val STATE_PLAYING = 2
        private val STATE_PAUSED = 3
        const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton
    lateinit var timer: TextView

    private var mainThreadHandler: Handler? = null
    var time = 30


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
        mainThreadHandler = Handler(Looper.getMainLooper())

        playButton.setOnClickListener { playbackControl() }
        pauseButton.setOnClickListener { playbackControl() }

        val arrowButton = findViewById<ImageView>(R.id.playerBackButtonArrow)
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
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        mainThreadHandler?.post(
            timing()
        )
    }

    private fun pausePlayer() {
        super.onPause()
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onPause(){
        super.onPause()
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }
    override fun onResume(){
        super.onResume()
        mediaPlayer.start()
    }

    private fun timing(): Runnable {
        return object : Runnable {

            override fun run() {
                if (playerState == STATE_PLAYING) {
                    if (time > 0) {
                        time -= 1
                        val postTime = time.toString()
                        if (time < 10) timer.text = "00:0$postTime" else timer.text = "00:$postTime"
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }
                }
            }
        }
    }

    private fun toaster(context: Context, text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }
}