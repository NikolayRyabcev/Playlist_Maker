package com.example.playlistmaker.ui.mediaLibrary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.adapter.TrackViewHolder

class PlaylistAdapter(
    private var plalists: List<Playlist>,
    private val clickListener: PlaylistClick
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return plalists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(plalists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(plalists[position])
            notifyDataSetChanged()
        }
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

    fun setItems(items: List<Playlist>) {
        plalists = items
        notifyDataSetChanged()
    }
}