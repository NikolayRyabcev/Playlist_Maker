package com.example.playlistmaker.ui.mediaLibrary.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Playlist

class PlaylistAdapter (private val plalists : List<Playlist>) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
}