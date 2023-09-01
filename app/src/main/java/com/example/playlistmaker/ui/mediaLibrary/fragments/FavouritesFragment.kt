package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesBinding


class FavouritesFragment : Fragment() {

    private lateinit var nullableFavouritesBinding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("onCreateView", "FavouritesFragment")
        nullableFavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return nullableFavouritesBinding.root
    }
}