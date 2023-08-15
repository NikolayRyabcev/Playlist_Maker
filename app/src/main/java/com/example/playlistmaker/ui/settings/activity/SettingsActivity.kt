package com.example.playlistmaker.ui.settings.activity


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем viewmodel
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        //кнопка назад
        binding.backButtonArrow.setOnClickListener {
            settingsViewModel.onBackClick()
        }
        settingsViewModel.getOnBackLiveData()
            .observe(this) { onBackLiveData -> onBackClick(onBackLiveData) }


        // обновление темы
        binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        var logger: String
        if (binding.simpleSwitch.isChecked) logger="true" else logger="false"
        Log.d("Темная", logger)
        binding.simpleSwitch.setOnClickListener {
            settingsViewModel.themeSwitch()
            binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
            if (binding.simpleSwitch.isChecked) logger="true" else logger="false"
            Log.d("Темная", logger)
        }

        //Поделиться
        binding.ShareAppText.setOnClickListener {
            settingsViewModel.shareApp()
        }

        //Написать в поддержку
        binding.WriteSupportText.setOnClickListener {
            settingsViewModel.writeSupport()
        }

        //share
        binding.AgreementText.setOnClickListener {
            settingsViewModel.readAgreement ()
        }
    }

    private fun onBackClick(back: Boolean) {
        if (back) {
            finish()
        }
    }
}


