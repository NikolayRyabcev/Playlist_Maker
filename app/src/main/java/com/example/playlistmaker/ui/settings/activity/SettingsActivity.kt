package com.example.playlistmaker.ui.settings.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем viewmodel

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


