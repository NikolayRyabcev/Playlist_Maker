package com.example.playlistmaker.ui.player.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistTrackerBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetAdapter(
    private var playlists: List<Playlist>,
    private val clickListener: PlaylistClick
) : RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBottomSheetViewHolder(PlaylistTrackerBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {

        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlists[position])
            notifyDataSetChanged()
        }
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }
}