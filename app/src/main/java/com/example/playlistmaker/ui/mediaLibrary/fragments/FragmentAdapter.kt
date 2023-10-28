package com.example.playlistmaker.ui.mediaLibrary.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.mediaLibrary.fragments.playlist.PlaylistFragment

class FragmentAdapter(
    parentFragment: Fragment
) :
    FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FavouritesFragment.newInstance()
        } else {
            PlaylistFragment.newInstance()
        }
    }

}