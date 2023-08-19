package com.example.playlistmaker.ui.settings.activity


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.search.view_model_for_activity.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем viewmodel


        //кнопка назад
        binding.backButtonArrow.setOnClickListener {
            settingsViewModel.onBackClick()
        }
        settingsViewModel.getOnBackLiveData()
            .observe(this) { onBackLiveData -> onBackClick(onBackLiveData) }


        // обновление темы
        binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
        binding.simpleSwitch.setOnClickListener {
            settingsViewModel.themeSwitch()
            binding.simpleSwitch.isChecked = !(settingsViewModel.getThemeLiveData().value!!)
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


