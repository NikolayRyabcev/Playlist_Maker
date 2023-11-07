package com.example.playlistmaker.ui.mediaLibrary.fragments.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private lateinit var nullablePlaylistBinding: FragmentPlaylistsBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var playlistAdapter :PlaylistAdapter

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
            val navController = findNavController()
            navController.navigate(R.id.newPlaylistFragment)
        }

        //список плейлистов и переход на экраны плейлистов
        var playlistList = playlistViewModel.playlistList.value
        if (playlistList.isNullOrEmpty()) playlistList = emptyList()
        playlistAdapter = PlaylistAdapter(playlistList, clickAdapting(playlistList))
        val recyclerView = nullablePlaylistBinding.playlistList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.adapter = playlistAdapter


        if (playlistViewModel.playlistList.value.isNullOrEmpty()) nullablePlaylistBinding.playlistList.visibility =
            GONE

        nullablePlaylistBinding.playlistList.visibility = VISIBLE
        return nullablePlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistViewModel.playlistMaker().value.isNullOrEmpty()) {
                noPlaylist()
                return@observe
            } else {
                nullablePlaylistBinding.playlistList.adapter = PlaylistAdapter(playlistList) {}
                existPlaylist()
                return@observe
            }
        }
    }

    private fun noPlaylist() {
        nullablePlaylistBinding.emptyPlaylist.visibility = VISIBLE
        nullablePlaylistBinding.emptyPlaylistText.visibility = VISIBLE
        nullablePlaylistBinding.playlistList.visibility = GONE
    }

    private fun existPlaylist() {
        nullablePlaylistBinding.emptyPlaylist.visibility = GONE
        nullablePlaylistBinding.emptyPlaylistText.visibility = GONE
        nullablePlaylistBinding.playlistList.visibility = VISIBLE
    }

    private fun clickAdapting(item: Playlist) {
        val bundle = Bundle()
        bundle.putParcelable("playlist", item)
        val navController = findNavController()
        navController.navigate(R.id.action_playlistFragment_to_playlistScreen, bundle)
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}