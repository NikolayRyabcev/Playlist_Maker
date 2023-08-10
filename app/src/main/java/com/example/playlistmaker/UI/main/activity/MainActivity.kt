package com.example.playlistmaker.UI.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.media_library.Activities.MediaLibraryActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SearchButton.setOnClickListener {
            Log.d("MainActivityButton", "SearchButton setOnClickListener")
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }
        binding.MediaLibButtom.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaLibraryActivity::class.java
                )
            )
        }
        binding.SettingsButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }
}