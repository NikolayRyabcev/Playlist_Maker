package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment(){
    private var nullableFavouritesBinding : FragmentFavouritesBinding? = null
    private val favouritesBinding = nullableFavouritesBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouritesBinding=FragmentFavouritesBinding.inflate(inflater, container,false)
    return favouritesBinding.root
    }
}