package com.example.playlistmaker.ui.mediaLibrary.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.fragments.FavouritesFragment

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState==null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.favouriteTracks, FavouritesFragment())
                .commit()
        }
        binding.backButtonArrow.setOnClickListener {
            finish()
        }
    }
}