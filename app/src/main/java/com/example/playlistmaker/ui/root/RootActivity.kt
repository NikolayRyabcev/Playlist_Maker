package com.example.playlistmaker.ui.root

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.mediaLibrary.fragments.MediaLibraryFragment
import com.example.playlistmaker.ui.search.fragments.SearchFragment
import com.example.playlistmaker.ui.settings.fragments.SettingsFragment
import com.example.playlistmaker.ui.theme.PlaylistTheme
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistTheme {
                Surface {
                    Text("Hello world")
                }
            }
        }

    }
}