package com.example.playlistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.mediaLibrary.fragments.playlist.NewPlaylistFragment
import com.example.playlistmaker.ui.player.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: PlayerActivityBinding
    private var url = ""
    private lateinit var bottomNavigator: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActivityBinding.inflate(layoutInflater)

        binding.playerBackButtonArrow.setOnClickListener {
            closer()
        }


        //BottomSheet
        val bottomSheetContainer = binding.standardBottomSheet
        val overlay = binding.overlay
        val bottomSheetBehavior = BottomSheetBehavior
            .from(bottomSheetContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                overlay.visibility = View.GONE
                            }

                            else -> {
                                overlay.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )

        //нажатие на кнопку "добавить в плейлист"
        binding.playlistAddButton.setOnClickListener {
            bottomSheetBehavior.state = STATE_HIDDEN
            binding.overlay.visibility = VISIBLE
        }

        //нажатие на кнопку "новый плейлист"
        binding.newPlaylistButton.setOnClickListener {
            val walkerToNewPlaylistFragment = NewPlaylistFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.rootContainer, walkerToNewPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //принятие и отрисовка данных трека
        val track = arguments?.getParcelable<Track>("track")
        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.time.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName ?: "Unknown Album"
        binding.year.text = (track?.releaseDate ?: "Year").take(4)
        binding.genre.text = track?.primaryGenreName ?: "Unknown Genre"
        binding.country.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        val radius = 8
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.bfplaceholder)
                .transform(RoundedCorners(radius))
                .into(binding.trackCover)
        }
        url = track?.previewUrl ?: return

        //создание плеера
        playerViewModel.createPlayer(url)

        //переключение кнопок плэй/пауза
        binding.playButton.isEnabled = false
        binding.playButton.setOnClickListener {
            if (playerViewModel.stateLiveData.value == PlayerState.STATE_PLAYING) playerViewModel.pause() else playerViewModel.play()
        }

        updateButton()

        playerViewModel.putTime().observe(requireActivity()) { timer ->
            binding.trackTimer.text = timer
            Log.d("время в активити", timer)
        }
        //нажатие на кнопку нравится
        binding.favourites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }

        playerViewModel.favouritesChecker(track).observe(requireActivity()) { favourtitesIndicator ->
            if (favourtitesIndicator) {
                binding.favourites.setImageResource(R.drawable.button__like)
            } else binding.favourites.setImageResource(
                R.drawable.favourites
            )
        }

        //список плейлистов
        val recyclerView = binding.playlistRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter =
            playerViewModel.playlistList.value?.let {
                PlaylistBottomSheetAdapter(it) {
                    playerViewModel.playlistAdding
                    playerViewModel.playlistAdding.observe(viewLifecycleOwner) {playlistAdding ->
                        if (playlistAdding) {
                            val toastMessage = "Трек уже добавлен в плейлист $it"
                            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            val toastMessage = "Добавлено в плейлист $it"
                            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
//        if (playerViewModel.playlistList.value.isNullOrEmpty()) binding.playlistRecycler.visibility =
//            View.GONE
        playerViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistList.isNullOrEmpty()) return@observe
            binding.playlistRecycler.adapter = PlaylistBottomSheetAdapter(playlistList) {
                playerViewModel.addTrack(track, it)
            }
        }

        //кнопка создать плейлист
        binding.newPlaylistButton.setOnClickListener {
            val walkerToNewPlaylistFragment = NewPlaylistFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootContainer, walkerToNewPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }

    private fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    fun playerStateDrawer() {

        playerViewModel.stateLiveData.observe(requireActivity()) {
            when (playerViewModel.stateLiveData.value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 0.5f

                }

                PlayerState.STATE_PREPARED -> {
                    preparePlayer()
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 1f

                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.playButton.alpha = 1f

                }

                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause)

                }

                else -> {
                }
            }
        }
    }

    private fun updateButton() {
        lifecycleScope.launch {
            delay(PLAYER_BUTTON_PRESSING_DELAY)
            playerStateDrawer()
        }
    }

    private fun closer() {
        val fragmentmanager = requireActivity().supportFragmentManager
        fragmentmanager.popBackStack()
    }
    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}
