package com.example.playlistmaker.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.search.models.Track


class TrackViewHolder(private val binding:TrackLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        binding.trackName.text = item.trackName
        binding.groupName.text = item.artistName
        binding.trackTime.text = item.trackTimeMillis
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.musicalbum)
            .into(binding.trackImage)
    }

}
