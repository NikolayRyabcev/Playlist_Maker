package com.example.playlistmaker.UI.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.media_library.Activities.MediaLibraryActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        binding.SearchButton.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        binding.MediaLibButtom.setOnClickListener { startActivity(Intent(this, MediaLibraryActivity::class.java)) }
        binding.SettingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}