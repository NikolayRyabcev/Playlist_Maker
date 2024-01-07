package com.example.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    @Composable
    @Preview
    fun settingsView() {
        val context = LocalContext.current
        val backgroundColor = ContextCompat.getColor(context, R.color.white)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(backgroundColor))
                .padding(
                    start = dimensionResource(id = R.dimen.left1)
                )
        ) {
            SettingsText()
        }
    }

    @Composable
    fun SettingsText() {
        val context = LocalContext.current
        val textColor = ContextCompat.getColor(context, R.color.black)
        Text(
            text = stringResource(id = R.string.SettingsString),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.settingsTopString14dp),
                    bottom = 0.dp
                ),
            color = Color(textColor),
            )
    }
}