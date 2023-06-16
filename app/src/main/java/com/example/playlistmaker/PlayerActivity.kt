package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        val arrowButton = findViewById<ImageView>(R.id.playerBackButtonArrow)
        arrowButton.setOnClickListener {
            finish()
        }
    }
}