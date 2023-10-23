package com.example.playlistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPlaylistFragment : Fragment() {
    private lateinit var newPlaylistBinding : NewPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newPlaylistBinding = NewPlaylistBinding.inflate(inflater,container,false)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility=GONE
        newPlaylistBinding.playlistNameEditText.setOnFocusChangeListener { _, hasFocus ->
            newPlaylistBinding.playlistName.hint = if (hasFocus) "Название*" else null  }
        return newPlaylistBinding.root
    }
}