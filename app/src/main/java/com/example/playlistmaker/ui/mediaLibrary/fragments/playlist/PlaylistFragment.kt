package com.example.playlistmaker.ui.mediaLibrary.fragments.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentPlaylistsBinding
    private lateinit var bottomNavigator: BottomNavigationView


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
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.VISIBLE

        return nullablePlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistViewModel.playlistMaker().value.isNullOrEmpty()) {
                noPlaylist()
            } else {
                existPlaylist()
            }
        }
    }

    private fun noPlaylist(){
        nullablePlaylistBinding.refreshButton.visibility=VISIBLE
        nullablePlaylistBinding.emptyPlaylist.visibility=VISIBLE
        nullablePlaylistBinding.emptyPlaylistText.visibility=VISIBLE
    }

    private fun existPlaylist(){
        nullablePlaylistBinding.refreshButton.visibility=GONE
        nullablePlaylistBinding.emptyPlaylist.visibility=GONE
        nullablePlaylistBinding.emptyPlaylistText.visibility=GONE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}