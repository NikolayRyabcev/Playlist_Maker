package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val srchButton = findViewById<Button>(R.id.SearchButton)
        val mediaLibButton = findViewById<Button>(R.id.MediaLibButtom)
        val settButton = findViewById<Button>(R.id.SettingsButton)

        srchButton.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        mediaLibButton.setOnClickListener { startActivity(Intent(this, MediaLibrary::class.java)) }
        settButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}