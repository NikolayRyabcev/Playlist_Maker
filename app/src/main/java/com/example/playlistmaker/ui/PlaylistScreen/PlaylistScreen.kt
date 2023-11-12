package com.example.playlistmaker.ui.PlaylistScreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistScreenFragmentBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreen : Fragment() {

    private lateinit var binding: PlaylistScreenFragmentBinding
    private val playlistScreenViewModel by viewModel<PlaylistScreenViewModel>()
    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistScreenFragmentBinding.inflate(inflater, container, false)
        //Нижний навигатор
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = GONE

        //отработка на кнопку назад
        binding.playlistBackButtonArrow.setOnClickListener {
            onBackClick()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackClick()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //принятие и отрисовка данных трека
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        binding.PlaylistName.text = playlist?.playlistName ?: "Unknown Playlist"
        binding.descriptionOfPlaylist.text = playlist?.description ?: ""
        if (playlist != null) {
            playlistTime(playlist)
        }

        //сколько треков в плейлисте
        val trackCounter = (playlist?.arrayNumber).toString()
        val text = when {
            trackCounter.toInt() % 10 == 1 && trackCounter.toInt() % 100 != 11 -> " трек"
            trackCounter.toInt() % 10 == 2 && trackCounter.toInt() % 100 != 12 -> " трека"
            trackCounter.toInt() % 10 == 3 && trackCounter.toInt() % 100 != 13 -> " трека"
            trackCounter.toInt() % 10 == 4 && trackCounter.toInt() % 100 != 14 -> " трека"
            else -> " треков"
        }
        binding.trackNumber.text = "$trackCounter $text"
        ///обложка
        val baseWidth = 312
        val baseHeight = 312
        val getImage = (playlist?.uri ?: "Unknown Cover")
        if (getImage != "Unknown Cover") {
            binding.playlistPlaceHolder.visibility = GONE
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .placeholder(R.drawable.bfplaceholder)
                .override(baseWidth, baseHeight)
                .into(binding.playlistCover)
        }

        //BottomSheet
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.trackInPlaylistContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

       /* binding.share.post {
            val screenHeight = binding.root.height
            Log.d("БоттомШит", screenHeight.toString())
            val peekHeight = screenHeight - binding.share.bottom
            Log.d("БоттомШит", peekHeight.toString())
            bottomSheetBehavior.peekHeight = peekHeight
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }*/
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


        //список треков в плейлисте
        trackAdapter = TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = {
                if (playlist != null) {
                    suggestTrackDeleting(it, playlist)
                }
            })
        if (playlist != null) {
            playlistScreenViewModel.getTrackList(playlist)
        }
        trackListMaker()
        binding.trackInPlaylistRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.trackInPlaylistRecycler.adapter = trackAdapter

        //кнопки

        binding.share.setOnClickListener {
            if (playlist != null) {
                sharePlaylist(playlist)
            }
        }
        binding.shareText.setOnClickListener {
            if (playlist != null) {
                sharePlaylist(playlist)
            }
        }
        binding.editInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            val navController = findNavController()
            navController.navigate(R.id.action_playlistScreen_to_editPlaylist, bundle)

        }
        binding.deletePlaylistInfo.setOnClickListener {
            if (playlist != null) {
                suggestPlaylistDeleting(playlist)
            }
        }

        //BottomSheet от кнопки Меню
        val menuBottomSheetContainer = binding.menuContainer
        val overlay = binding.overlay
        val menuBottomSheetBehavior = BottomSheetBehavior
            .from(menuBottomSheetContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        menuBottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                overlay.visibility = GONE
                            }

                            else -> {
                                overlay.visibility = VISIBLE
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )

        binding.more.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun clickAdapting(item: Track) {
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        val navController = findNavController()
        navController.navigate(R.id.action_playlistScreen_to_playerFragment, bundle)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackListMaker() {
        Log.d ("Заброшено во вью", "trackListMaker")
        playlistScreenViewModel.trackList.observe(viewLifecycleOwner) { trackList ->
            if (trackList.isNullOrEmpty()) binding.emptyList.visibility= VISIBLE else {
                trackAdapter.setItems(trackList)
                Log.d ("Заброшено во вью", trackList.toString())
                trackAdapter.notifyDataSetChanged()
                binding.emptyList.visibility= GONE
                return@observe
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTrackByClick(item: Track, playlist: Playlist) {
        playlistScreenViewModel.deleteTrack(item, playlist)
        playlistScreenViewModel.getTrackList (playlist)
        trackListMaker()
        trackAdapter.notifyDataSetChanged()

    }

    private fun deletePlaylist(item: Playlist) {
        playlistScreenViewModel.deletePlaylist(item)
        val navController = findNavController()
        navController.navigate(R.id.action_playlistScreen_to_mediaLibraryFragment2)
    }

    private fun suggestPlaylistDeleting(playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = playlistScreenViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deletePlaylist(playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun suggestTrackDeleting(track: Track, playlist: Playlist) {
        val textColor: Int
        val isDarkTheme = playlistScreenViewModel.isAppThemeDark()
        textColor = if (isDarkTheme) {
            Color.BLACK
        } else {
            Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Вы уверены, что хотите удалить трек из плейлиста??")
            .setNegativeButton("Нет") { _, _ ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { _, _ ->
                deleteTrackByClick(track, playlist)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
    }

    private fun sharePlaylist(playlist: Playlist) {
        playlistScreenViewModel.sharePlaylist(playlist)
    }

    private fun onBackClick() {
        val fragmentmanager = requireActivity().supportFragmentManager
        bottomNavigator.visibility = VISIBLE
        fragmentmanager.popBackStack()
    }

    fun playlistTime(playlist:Playlist) {
        playlistScreenViewModel.getPlaylistTime(playlist)
        playlistScreenViewModel.playlistTime.observe(viewLifecycleOwner) {
            playlistTime -> binding.playlistTime.text = playlistTime
            Log.d("Время ui", playlistTime)
        }
    }
}