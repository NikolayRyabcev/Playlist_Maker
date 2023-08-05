package com.example.playlistmaker.UI.settings.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var themeSwitcher : Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        //делаем viewmodel
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        //кнопка назад
        val arrowButton = findViewById<ImageView>(R.id.searchButtonArrow)
        arrowButton.setOnClickListener {
            settingsViewModel.onBackClick()
        }
        settingsViewModel.getOnBackLiveData()
            .observe(this) { onBackLiveData -> onBackClick(onBackLiveData) }


        // обновление темы
        themeSwitcher = findViewById(R.id.simpleSwitch)
        settingsViewModel.getthemeLiveData().value?.let { themeSwitcher.isChecked = it }
        themeSwitcher.setOnClickListener {
            settingsViewModel.themeSwitch()
        }
        settingsViewModel.getthemeLiveData()
            .observe(this) { themeLiveData -> changeTheme(themeLiveData) }


        //Поделиться
        val textSendView = findViewById<FrameLayout>(R.id.ShareAppText)
        textSendView.setOnClickListener {
            settingsViewModel.shareApp()
        }

        //Написать в поддержку
        val textSendToView2 = findViewById<FrameLayout>(R.id.WriteSupportText)
        textSendToView2.setOnClickListener {
            settingsViewModel.writeSupport()
        }

        //share
        val textAgreementView3 = findViewById<FrameLayout>(R.id.AgreementText)
        textAgreementView3.setOnClickListener {
            settingsViewModel.readAgreement ()
        }
    }

    private fun onBackClick(back: Boolean) {
        if (back) {
            finish()
        }

    }

    fun changeTheme(theme: Boolean) {
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Log.d("darkmode", "night1")
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            Log.d("darkmode", "night2")
        }

    }
}


