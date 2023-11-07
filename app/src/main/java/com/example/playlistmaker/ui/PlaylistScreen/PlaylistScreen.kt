package com.example.playlistmaker.ui.PlaylistScreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistScreenFragmentBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions3.RxPermissions

class PlaylistScreen : Fragment() {
    lateinit var binding: PlaylistScreenFragmentBinding
    private lateinit var bottomNavigator: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistScreenFragmentBinding.inflate(inflater, container, false)
        //Нижний навигатор
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

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
        binding.PlaylistName.text = playlist?.playlistName?: "Unknown Playlist"
        binding.descriptionOfPlaylist.text = playlist?.description?: ""
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
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.bfplaceholder)
                .override(baseWidth, baseHeight)
                .into(binding.playlistCover)
        }

        val rxPermissions = RxPermissions(this)

        //переменеая с лямбдой, которая берет изображение и сохраняет его в ханилище
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val width = 236
                    val height = 236
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(CenterCrop())
                        .override(width, height)
                        .into(binding.playlistCover)
                    //saveImageToPrivateStorage(uri)

                } else {
                    //ничего не делаем
                }
            }
        //обработка нажатия на область обложки
        binding.playlistCover.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        // Пользователь отказал, ничего не делаем
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
        }
    }

    private fun onBackClick() {
        val fragmentmanager = requireActivity().supportFragmentManager
        bottomNavigator.visibility = View.VISIBLE
        fragmentmanager.popBackStack()
    }

}