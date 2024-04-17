package com.example.playlistmaker.ui.player.fragment

import MusicService
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.ConnectionBroadcastReciever
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
    private val connectionBroadcastReciever = ConnectionBroadcastReciever()
    private var musicService: MusicService? = null

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

        ContextCompat.registerReceiver(
            requireContext(),
            connectionBroadcastReciever,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        //переключение кнопок плэй/пауза
        binding.playButton.isEnabled = false
        binding.playButton.setOnClickListener { playerViewModel.onPlayerButtonClicked() }
        //привязка сервиса муз плеера
        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            updateButton()
            binding.trackTimer.text = musicService?.getCurrentPlayerPosition() ?: "00:00"
        }
        bindMusicService(requireContext())
        return binding.root
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

        updateButton()

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

    override fun onStop() {
        bottomNavigator.visibility = VISIBLE
        super.onStop()
    }

    override fun onDestroy() {
        unBindMusicService()
        super.onDestroy()
        playerViewModel.removeAudioPlayerControl()
    }

    @SuppressLint("ResourceType")
    fun playerStateDrawer() {
        playerViewModel.playerState.observe(requireActivity()) {
            when (playerViewModel.playerState.value) {
                is PlayerState.Default -> {
                    binding.playButton.alpha = 0.5f
                }

                is PlayerState.Prepared -> {
                    if (isFirstPlay) {
                        /*                        binding.playButton.onTouchListener =
                                                    { playerViewModel.onPlayerButtonClicked() }*/
                        isFirstPlay = false
                        binding.playButton.alpha = 1f
                    } else {
                        binding.playButton.onStopped()
                    }
                }

                is PlayerState.Paused -> {
                    binding.playButton.alpha = 1f
                }

                is PlayerState.Playing -> {
                }

                null -> {}
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

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            playerViewModel.setAudioPlayerControl(binder.getMusicService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerViewModel.removeAudioPlayerControl()
        }
    }

    private fun bindMusicService(context: Context) {
        Log.d("плеер", "bindService")
        val intent = Intent(context, MusicService::class.java).apply {
            putExtra("song_url", url)
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unBindMusicService() {
        requireActivity().unbindService(serviceConnection)
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}