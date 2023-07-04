package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {
    companion object {
        private val STATE_DEFAULT = 0
        private val STATE_PREPARED = 1
        private val STATE_PLAYING = 2
        private val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private lateinit var playButton:ImageButton
    lateinit var pauseButton:ImageButton

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
        pauseButton = findViewById(R.id.playButton)

        playButton.setOnClickListener{playbackControl()}
        pauseButton.setOnClickListener{playbackControl()}

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

    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            playButton.isEnabled=true
            playerState=STATE_PREPARED
            playButton.visibility= View.GONE
            playButton.visibility= View.VISIBLE

        }
        mediaPlayer.setOnCompletionListener{
            playerState=STATE_PREPARED
            playButton.visibility= View.VISIBLE
            playButton.visibility= View.GONE
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState=STATE_PLAYING
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState=STATE_PAUSED
    }

    private fun playbackControl (){
        when (playerState) {
            STATE_PLAYING ->{pausePlayer()}
            STATE_PREPARED, STATE_PAUSED ->{startPlayer()}
        }
    }
}