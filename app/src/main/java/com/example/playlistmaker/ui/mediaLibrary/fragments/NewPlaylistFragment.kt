package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.Manifest
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPlaylistFragment : Fragment() {
    private lateinit var newPlaylistBinding: NewPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
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

            //проверяем, есть ли доступ к файлам на устройстве
            var imagePermission = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_MEDIA_IMAGES
            )


            if (imagePermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    READ_MEDIA_IMAGES_REQUEST_CODE
                )
            }
            imagePermission = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
            if (imagePermission == PackageManager.PERMISSION_GRANTED) {
                // создаём событие с результатом и передаём в него PickVisualMedia()
                val pickMedia =
                    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                        // Callback вызовется, когда пользователь выберет картинку
                        if (uri != null) {
                            Log.d("PhotoPicker", "Выбранный URI: $uri")
                        } else {
                            Log.d("PhotoPicker", "Ничего не выбрано")
                        }
                    }
// Вызываем метод launch и передаём параметр, чтобы предлагались только картинки
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        return newPlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!newPlaylistBinding.playlistNameEditText.text.isNullOrEmpty()) {
            newPlaylistBinding.createButton.backgroundTintList =
                (ContextCompat.getColorStateList(requireContext(), R.color.back1))
            newPlaylistBinding.createButton.isEnabled=true
        } else {
            newPlaylistBinding.createButton.backgroundTintList =
                (ContextCompat.getColorStateList(requireContext(), R.color.settingsIconGray))
            newPlaylistBinding.createButton.isEnabled=false
        }
    }
}