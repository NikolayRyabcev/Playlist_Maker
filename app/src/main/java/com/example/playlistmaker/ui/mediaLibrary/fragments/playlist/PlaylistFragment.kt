package com.example.playlistmaker.ui.mediaLibrary.fragments.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
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
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = VISIBLE

        //кнопка создать плейлист
        nullablePlaylistBinding.newPlaylistButton.setOnClickListener {
            val walkerToNewPlaylistFragment = NewPlaylistFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootContainer, walkerToNewPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //список плейлистов
        val recyclerView = nullablePlaylistBinding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter= playlistViewModel.playlistList.value?.let { PlaylistAdapter(it, {}) }
        if (playlistViewModel.playlistList.value.isNullOrEmpty()) nullablePlaylistBinding.playlistList.visibility=GONE

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
        nullablePlaylistBinding.emptyPlaylist.visibility=VISIBLE
        nullablePlaylistBinding.emptyPlaylistText.visibility=VISIBLE
        nullablePlaylistBinding.playlistList.visibility=GONE
    }

    private fun existPlaylist(){
        nullablePlaylistBinding.emptyPlaylist.visibility=GONE
        nullablePlaylistBinding.emptyPlaylistText.visibility=GONE
        nullablePlaylistBinding.playlistList.visibility=VISIBLE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}