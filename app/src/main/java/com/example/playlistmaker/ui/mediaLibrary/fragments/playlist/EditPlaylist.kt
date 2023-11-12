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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EditPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.mediaLibrary.viewModels.playlist.EditPlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylist: Fragment() {
    private lateinit var editPlaylistBinding: EditPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private val viewModel: EditPlaylistViewModel by viewModel()
    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editPlaylistBinding = EditPlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE


        return editPlaylistBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        val name = editPlaylistBinding.playlistNameEditText
        if (playlist != null) {
            name.setText( playlist.playlistName)
        }
        if (playlist != null) {
            editPlaylistBinding.playlistDescriptEditText.setText(playlist.description)
        }
        ///обложка
        val baseWidth = 312
        val baseHeight = 312
        val getImage = (playlist?.uri ?: "Unknown Cover")
        if (getImage != "Unknown Cover") {
            editPlaylistBinding.playlistPlaceHolder.visibility = View.GONE
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .placeholder(R.drawable.bfplaceholder)
                .override(baseWidth, baseHeight)
                .into(editPlaylistBinding.playlistCover)
        }


        val rxPermissions = RxPermissions(this)

        //отработка на кнопку назад
        editPlaylistBinding.playlistBackButtonArrow.setOnClickListener {
            onBackClick()
        }

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
        editPlaylistBinding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)

        //переменеая с лямбдой, которая берет изображение и сохраняет его в ханилище
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val radius = 8
                    val width = 312
                    val height = 312
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(CenterCrop(), RoundedCorners(radius))
                        .override(width, height)
                        .into(editPlaylistBinding.playlistCover)
                    saveImageToPrivateStorage(uri)

                } else {
                    //ничего не делаем
                }
            }

        //обработка нажатия на область обложки
        editPlaylistBinding.playlistCover.setOnClickListener {
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackClick()
        }
        editPlaylistBinding.saveButton.setOnClickListener {
            if (editPlaylistBinding.playlistNameEditText.text.toString()
                    .isEmpty()
            ) return@setOnClickListener
            if (playlist != null) {
                savePlaylist(playlist)
            }
        }

    }
    private fun onBackClick() {
        val name = editPlaylistBinding.playlistNameEditText.text
        val descr = editPlaylistBinding.playlistDescriptEditText.text
        closer()
    }

    private fun closer() {
        val fragmentmanager = requireActivity().supportFragmentManager
        fragmentmanager.popBackStack()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover_${fileCount + 1}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        editPlaylistBinding.playlistPlaceHolder.visibility = View.GONE
        selectedUri = file.toUri()
    }

    private fun turnOffCreateButton() {
        editPlaylistBinding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.settingsIconGray))
        editPlaylistBinding.saveButton.isEnabled = false
    }

    private fun turnOnCreateButton() {
        editPlaylistBinding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.back1))
        editPlaylistBinding.saveButton.isEnabled = true
    }

    private fun savePlaylist(playlist: Playlist) {
        viewModel.savePlayList(
            editPlaylistBinding.playlistNameEditText.text.toString(),
            editPlaylistBinding.playlistDescriptEditText.text.toString(),
            selectedUri.toString(),
        )
        viewModel.deletePlaylist(playlist)
        closer()
    }
}