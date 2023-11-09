package com.example.playlistmaker.ui.PlaylistScreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.tbruyelle.rxpermissions3.RxPermissions
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
    ): View? {
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
        binding.playlistTime.text = "" //посчитать и дописвть
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

        val rxPermissions = RxPermissions(this)
        //BottomSheet


        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.trackInPlaylistContainer)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        val screenHeight = binding.root.height
        bottomSheetBehavior.peekHeight = screenHeight - binding.more.bottom
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        /*bottomSheetBehavior
            .addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_COLLAPSED -> {

                            }

                            else -> {

                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                }
            )*/

        //список треков в плейлисте
        trackAdapter = TrackAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = { deleteTrackByClick(it) })
        trackAdapter.setItems(trackListMaker())

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
            //ТУДУ()
        }
        binding.deletePlaylistInfo.setOnClickListener {
            suggestDeleting()
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

        binding.more.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun clickAdapting(item: Track) {
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        val navController = findNavController()
        navController.navigate(R.id.action_searchFragment_to_playerFragment, bundle)
    }

    private fun trackListMaker(): List<Track> { //добавить запрос треков из базы
        return emptyList()
    }

    private fun deleteTrackByClick(item: Track) {
        //Туду
    }

    private fun deletePlaylist() {
        //ТУДУ()
    }

    private fun suggestDeleting() {
        val textColor: Int
        val isDarkTheme = playlistScreenViewModel.isAppThemeDark()
        if (isDarkTheme) {
            textColor = Color.BLACK
        } else {
            textColor = Color.WHITE
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { dialog, which ->
                return@setNegativeButton
            }
            .setPositiveButton("Да") { dialog, which ->
                deletePlaylist()
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
        bottomNavigator.visibility = View.VISIBLE
        fragmentmanager.popBackStack()
    }

}