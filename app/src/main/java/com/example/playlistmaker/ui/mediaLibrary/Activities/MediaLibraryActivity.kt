package com.example.playlistmaker.ui.mediaLibrary.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.mediaLibrary.fragments.FavouritesFragment
import com.example.playlistmaker.ui.mediaLibrary.fragments.FragmentAdapter
import com.example.playlistmaker.ui.mediaLibrary.fragments.SelectPage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity(), SelectPage {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        binding.backButtonArrow.setOnClickListener {
            finish()
        }
        setContentView(binding.root)

        val adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Вкладка 1"
                1 -> tab.text = "Вкладка 2"
            }
        }
        tabMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val currentPosition = tab.position
                    binding.pager.currentItem = currentPosition
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )

    }


    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}