package com.example.playlistmaker.ui.mediaLibrary.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.mediaLibrary.fragments.FavouritesFragment

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
        if (savedInstanceState==null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.favouriteTracks, FavouritesFragment)
                .commit()
        }
    }
}