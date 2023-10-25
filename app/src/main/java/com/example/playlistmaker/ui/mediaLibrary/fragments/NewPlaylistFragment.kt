package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPlaylistFragment : Fragment() {
    private lateinit var newPlaylistBinding: NewPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Пользователь дал разрешение, идем во вью-модель загружать фото
                loadImage()
                Log.d("Разрешение на загрузку", "дано")

            } else {
                // Пользователь отказал, ничего не делаем
                Log.d("Разрешение на загрузку", "отказано")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newPlaylistBinding = NewPlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = GONE

        //обработка нажатия на область обложки
        newPlaylistBinding.playlistCover.setOnClickListener {
            if (checkImagePermission()) loadImage() else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_MEDIA_IMAGES_REQUEST_CODE
                )
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        }

        return newPlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    private fun loadImage() {}

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

    private fun checkImagePermission(): Boolean {
        val permissionProvided = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES
        )
        return permissionProvided == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val READ_MEDIA_IMAGES_REQUEST_CODE = 1
    }
}