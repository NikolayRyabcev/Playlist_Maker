package com.example.playlistmaker.UI.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.media_library.Activities.MediaLibraryActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val srchButton = findViewById<Button>(R.id.SearchButton)
        val mediaLibButton = findViewById<Button>(R.id.MediaLibButtom)
        val settButton = findViewById<Button>(R.id.SettingsButton)

        srchButton.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        mediaLibButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaLibraryActivity::class.java
                )
            )
        }
        settButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}