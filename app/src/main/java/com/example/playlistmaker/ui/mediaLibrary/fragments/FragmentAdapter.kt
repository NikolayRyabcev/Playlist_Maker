package com.example.playlistmaker.ui.mediaLibrary.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter (hostFragment: Fragment) : FragmentStateAdapter(hostFragment) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return if (position ==0) FavouritesFragment() else PlaylistFragment()
    }
}