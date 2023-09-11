package com.example.playlistmaker.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.mediaLibrary.fragments.MediaLibraryFragment
import com.example.playlistmaker.ui.search.fragments.SearchFragment
import com.example.playlistmaker.ui.settings.fragments.SettingsFragment

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.rootContainer, MediaLibraryFragment())
            }
            supportFragmentManager.commit {
                this.add(R.id.rootContainer, SearchFragment())
            }
            supportFragmentManager.commit {
                this.add(R.id.rootContainer, SettingsFragment())
            }
        }
    }
}