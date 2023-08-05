package com.example.playlistmaker.UI.main.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.main.view_model.MainViewModel
import com.example.playlistmaker.UI.media_library.Activities.MediaLibraryActivity
import com.example.playlistmaker.UI.search.activity.SearchActivity
import com.example.playlistmaker.UI.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //делаем viewmodel
        mainViewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory()
        )[MainViewModel::class.java]

        //вызываем кнопки
        val searchButton = findViewById<Button>(R.id.SearchButton)
        val mediaLibButton = findViewById<Button>(R.id.MediaLibButtom)
        val settButton = findViewById<Button>(R.id.SettingsButton)

        //ставим обработку кликов
        searchButton.setOnClickListener { mainViewModel.pressSearch() }
        mediaLibButton.setOnClickListener { mainViewModel.pressMediaTech() }
        settButton.setOnClickListener { mainViewModel.pressSettings() }
    }
}