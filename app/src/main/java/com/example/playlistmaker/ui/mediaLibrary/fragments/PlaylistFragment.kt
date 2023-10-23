package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModels.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullablePlaylistBinding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        nullablePlaylistBinding.refreshButton.setOnClickListener {
            val walkerToNewPlaylistFragment = NewPlaylistFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootContainer, walkerToNewPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return nullablePlaylistBinding.root
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}