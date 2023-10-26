package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions3.RxPermissions

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

        //обработка нажатия на область обложки
        newPlaylistBinding.playlistCover.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        loadImage()
                        Log.d("Разрешение на загрузку", "дано")
                    } else {
                        // Пользователь отказал, ничего не делаем
                        Log.d("Разрешение на загрузку", "отказано")
                    }
                }
        }
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
}