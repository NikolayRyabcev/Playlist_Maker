package com.example.playlistmaker.ui.player.fragment

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.player.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: PlayerActivityBinding
    private var url = ""
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter
    private var isFirstPlay = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerActivityBinding.inflate(layoutInflater)
        //Нижний навигатор
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

        //нажатие на кнопку "новый плейлист"
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        //кнопка назад
        binding.playerBackButtonArrow.setOnClickListener {
            bottomNavigator.visibility = VISIBLE
            closer()
        }
        return binding.root
    }

    override fun onStop() {
        bottomNavigator.visibility = VISIBLE
        super.onStop()
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

        updateButton()

        playerViewModel.putTime().observe(requireActivity()) { timer ->
            binding.trackTimer.text = timer
        }

        //нажатие на кнопку нравится
        binding.favourites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }
        playerViewModel.favouritesChecker(track)
            .observe(requireActivity()) { favourtitesIndicator ->
                if (favourtitesIndicator) {
                    binding.favourites.setImageResource(R.drawable.button__like)
                } else binding.favourites.setImageResource(
                    R.drawable.favourites
                )
            }


        //BottomSheet
        val bottomSheetContainer = binding.standardBottomSheet
        val overlay = binding.overlay
        val bottomSheetBehavior = BottomSheetBehavior
            .from(bottomSheetContainer)
            .apply {
                state = STATE_HIDDEN
            }
        bottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            STATE_HIDDEN -> {
                                overlay.visibility = View.GONE
                            }

                            else -> {
                                overlay.visibility = VISIBLE
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )
        //нажатие на кнопку "добавить в плейлист"
        binding.playlistAddButton.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
            binding.overlay.visibility = VISIBLE
        }

        //список плейлистов
        if (!playerViewModel.playlistList.value.isNullOrEmpty()) {
            playlistAdapter = playerViewModel.playlistList.value?.let { it ->
                PlaylistBottomSheetAdapter(it) {
                    playlistClickAdapting(track, it)
                    bottomSheetBehavior.state = STATE_HIDDEN
                }
            }!!
        } else {
            playlistAdapter = PlaylistBottomSheetAdapter(emptyList()) {}
        }
        val recyclerView = binding.playlistRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = playlistAdapter

        playerViewModel.playlistMaker().observe(viewLifecycleOwner) { playlistList ->
            if (playlistList.isNullOrEmpty()) return@observe
            binding.playlistRecycler.adapter = PlaylistBottomSheetAdapter(playlistList) {
                playlistClickAdapting(track, it)
                bottomSheetBehavior.state = STATE_HIDDEN
            }
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
        binding.playButton.visibility = VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    fun playerStateDrawer() {

        playerViewModel.stateLiveData.observe(requireActivity()) {
            when (playerViewModel.stateLiveData.value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.alpha = 0.5f
                }

                PlayerState.STATE_PREPARED -> {
                    if (isFirstPlay) {
                        preparePlayer()
                        binding.playButton.onTouchListener = { togglePlayer() }
                        isFirstPlay = false
                        binding.playButton.alpha = 1f
                    } else {
                        binding.playButton.onStopped()
                        Log.d("КастомВью", "STATE_PREPARED")
                    }
                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.alpha = 1f
                }

                else -> {}
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
        bottomNavigator.visibility = VISIBLE
        fragmentmanager.popBackStack()
    }

    private fun playlistClickAdapting(track: Track, playlist: Playlist) {
        var trackIsAdded = false
        playerViewModel.addTrack(track, playlist)
        lifecycleScope.launch {
            delay(300)
            playerViewModel.playlistAdding.observe(viewLifecycleOwner) { playlistAdding ->
                val playlistName = playlist.playlistName
                if (!trackIsAdded) {
                    if (playlistAdding) {
                        val toastMessage = "Трек уже добавлен в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    } else {
                        val toastMessage = "Добавлено в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        return@observe
                    }
                }
            }
        }
    }

    private fun togglePlayer() {
        if (playerViewModel.stateLiveData.value == PlayerState.STATE_PLAYING) {
            playerViewModel.pause()
        } else {
            playerViewModel.play()
        }
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}