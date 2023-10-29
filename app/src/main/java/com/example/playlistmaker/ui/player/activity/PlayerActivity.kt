package com.example.playlistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.mediaLibrary.adapters.PlaylistAdapter
import com.example.playlistmaker.ui.mediaLibrary.fragments.playlist.NewPlaylistFragment
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: PlayerActivityBinding
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playerBackButtonArrow.setOnClickListener {
            finish()
        }

        //принятие и отрисовка данных трека
        val track = intent.getParcelableExtra<Track>("track")
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

        playerViewModel.putTime().observe(this) { timer ->
            binding.trackTimer.text = timer
            Log.d("время в активити", timer)
        }

        //нажатие на кнопку нравится
        binding.favourites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }

        playerViewModel.favouritesChecker(track).observe(this) { favourtitesIndicator ->
            Log.d("favourtitesIndicator", "$favourtitesIndicator")
            if (favourtitesIndicator) {
                binding.favourites.setImageResource(R.drawable.button__like)
            } else binding.favourites.setImageResource(
                R.drawable.favourites
            )
        }

        //BottomSheet
        val bottomSheetContainer = binding.standardBottomSheet

        val overlay = binding.overlay

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
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
        })
        bottomSheetBehavior.state=STATE_COLLAPSED

        //нажатие на кнопку "добавить в плейлист"
        binding.playlistAddButton.setOnClickListener {
            bottomSheetBehavior.state= STATE_EXPANDED
            binding.overlay.visibility=VISIBLE
        }

        //нажатие на кнопку "новый плейлист"
        binding.newPlaylistButton.setOnClickListener {
                val walkerToNewPlaylistFragment = NewPlaylistFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.rootContainer, walkerToNewPlaylistFragment)
                transaction.addToBackStack(null)
                transaction.commit()
        }

        //список плейлистов
        val recyclerView = binding.playlistRecycler
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter= playerViewModel.playlistList.value?.let { PlaylistAdapter(it, {}) }
        if (playerViewModel.playlistList.value.isNullOrEmpty()) binding.playlistRecycler.visibility=
            View.GONE
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

        playerViewModel.stateLiveData.observe(this) {
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

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}
