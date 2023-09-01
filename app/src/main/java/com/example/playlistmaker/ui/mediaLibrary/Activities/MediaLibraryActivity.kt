package com.example.playlistmaker.ui.mediaLibrary.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.fragments.FavouritesFragment
import com.example.playlistmaker.ui.mediaLibrary.fragments.FragmentAdapter
import com.example.playlistmaker.ui.mediaLibrary.fragments.SelectPage
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity(), SelectPage {
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.favouriteTracks, FavouritesFragment())
                .commit()
        }
        binding.backButtonArrow.setOnClickListener {
            finish()
        }
        val adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter
        tabMediator.attach()
    }

    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}