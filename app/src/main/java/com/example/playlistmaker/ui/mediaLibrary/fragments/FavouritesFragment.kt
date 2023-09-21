package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouritesFragment : Fragment() {
    //видимо, вью-модель понадобится в следующем спринте
    private val favouritesViewModel by viewModel<FavouritesViewModel>()

    private lateinit var nullableFavouritesBinding : FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return nullableFavouritesBinding.root
    }

    companion object {
        fun newInstance() = FavouritesFragment()

    }
}