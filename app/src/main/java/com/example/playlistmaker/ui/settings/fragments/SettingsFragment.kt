package com.example.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsFragment : Fragment() {
    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel.getOnBackLiveData()
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
            settingsViewModel.readAgreement()
        }
    }
}