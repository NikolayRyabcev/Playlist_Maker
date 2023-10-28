package com.example.playlistmaker.ui.mediaLibrary.fragments.playlist

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.NewPlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {
    private lateinit var newPlaylistBinding: NewPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    var isFileLoaded = false
    private val viewModel :NewPlaylistViewModel by viewModel()
    private var selectedUri: Uri? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newPlaylistBinding = NewPlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = GONE
        newPlaylistBinding.playlistBackButtonArrow.setOnClickListener {
            onBackClick()
        }
        newPlaylistBinding.createButton.setOnClickListener {
            createPlaylist()
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Плейлист $newPlaylistBinding.playlistNameEditText.text создан")
                .setNegativeButton("Оk") { dialog, which ->
                    closer()
                }
                .show()
        }
        return newPlaylistBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rxPermissions = RxPermissions(this)

        //устанавливаем цвет кнопки "Создать"
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                turnOffCreateButton()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                turnOnCreateButton()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    turnOffCreateButton()
                } else {
                    turnOnCreateButton()
                }
            }
        }
        newPlaylistBinding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)

        //переменеая с лямбдой, которая берет изображение и сохраняет его в ханилище
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val radius = 8
                    Glide.with(requireActivity())
                        .load(uri)
                        .placeholder(R.drawable.add_photo)
                        .transform(RoundedCorners(radius))
                        .into(newPlaylistBinding.playlistCover)
                    saveImageToPrivateStorage(uri)

                } else {
                    //ничего не делаем
                }
            }

        //обработка нажатия на область обложки
        newPlaylistBinding.playlistCover.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        Log.d("Разрешение на загрузку", "дано")
                    } else {
                        // Пользователь отказал, ничего не делаем
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        Log.d("Разрешение на загрузку", "отказано")
                    }
                }
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        Log.d("Разрешение на загрузку", "файл записан")
        newPlaylistBinding.playlistPlaceHolder.visibility = GONE
        isFileLoaded = true
        selectedUri=file.toUri()
    }

    private fun onBackClick() {
        if (isFileLoaded && !newPlaylistBinding.playlistNameEditText.text.isNullOrEmpty() && !newPlaylistBinding.playlistDescriptEditText.text.isNullOrEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->
                    return@setNegativeButton
                }
                .setPositiveButton("Завершить") { dialog, which ->
                    closer()
                }
                .show()
        } else {
            closer()
        }
    }

    private fun closer() {
        val fragmentmanager = requireActivity().supportFragmentManager
        fragmentmanager.popBackStack()
    }

    private fun turnOffCreateButton() {
        newPlaylistBinding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.settingsIconGray))
        newPlaylistBinding.createButton.isEnabled = false
    }

    private fun turnOnCreateButton() {
        newPlaylistBinding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.back1))
        newPlaylistBinding.createButton.isEnabled = true
    }

    private fun createPlaylist() {
        viewModel.addPlayList(Playlist(
            newPlaylistBinding.playlistNameEditText.text.toString(),
            newPlaylistBinding.playlistDescriptEditText.text.toString(),
            selectedUri.toString(),
            emptyList(),
            0
        ))
    }
}