package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import com.example.playlistmaker.ui.player.fargment.PlayerFragment
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouritesFragment : Fragment() {

    private val favouritesViewModel by viewModel<FavouritesViewModel>()
    private lateinit var nullableFavouritesBinding: FragmentFavouritesBinding

    private var isClickAllowed = true
    private val favouritesAdapter: TrackAdapter by lazy {
        TrackAdapter { clickAdapting(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableFavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)

        nullableFavouritesBinding.emptyMediaLibrary.visibility = View.GONE
        nullableFavouritesBinding.emptyMediaLibraryText.visibility = View.GONE

        nullableFavouritesBinding.favouritesRecycler.layoutManager =
            LinearLayoutManager(requireContext())
        nullableFavouritesBinding.favouritesRecycler.adapter = favouritesAdapter
        return nullableFavouritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.favouritesMaker().observe(viewLifecycleOwner) { trackResultList ->
            if (favouritesViewModel.trackResultList.value.isNullOrEmpty()) {
                nullableFavouritesBinding.emptyMediaLibrary.visibility = View.VISIBLE
                nullableFavouritesBinding.emptyMediaLibraryText.visibility = View.VISIBLE
                nullableFavouritesBinding.favouritesRecycler.visibility = GONE
                favouritesAdapter.notifyDataSetChanged()
            } else {
                nullableFavouritesBinding.emptyMediaLibrary.visibility = View.GONE
                nullableFavouritesBinding.emptyMediaLibraryText.visibility = View.GONE
                nullableFavouritesBinding.favouritesRecycler.visibility = VISIBLE
                favouritesAdapter.setItems(favouritesViewModel.trackResultList.value!!)

                favouritesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickAdapting(item: Track) {
        favouritesViewModel.addItem(item)
        val fragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rootContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }
}