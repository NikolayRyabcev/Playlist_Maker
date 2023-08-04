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


        //share
        val textSendView = findViewById<FrameLayout>(R.id.ShareAppText)
        textSendView.setOnClickListener {

        }

        //share
        val textSendToView2 = findViewById<FrameLayout>(R.id.WriteSupportText)
        textSendToView2.setOnClickListener {
            val intentSendTo2 = Intent(Intent.ACTION_SENDTO)
            intentSendTo2.data = Uri.parse("mailto:")
            val email = getString(R.string.myemail)
            intentSendTo2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intentSendTo2.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareText))
            startActivity(intentSendTo2)
        }

        //share
        val textAgreementView3 = findViewById<FrameLayout>(R.id.AgreementText)
        textAgreementView3.setOnClickListener {
            val intentAgreement3 =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.AgreementURL)))
            startActivity(intentAgreement3)
        }
    }

    private fun onBackClick(back: Boolean) {
        if (back) {
            finish()
        }

    }

    private fun changeTheme(theme: Boolean) {
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


