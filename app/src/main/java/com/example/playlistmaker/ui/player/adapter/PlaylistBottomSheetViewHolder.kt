package com.example.playlistmaker.ui.player.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetViewHolder(private val binding: PlaylistLayoutBinding) :RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistlittleName.text = item.playlistName
        binding.playlistlittleSongNumber.text = item.arrayNumber.toString()

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)
        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.musicalbum)
            .transform(RoundedCorners(radius))
            .into(binding.playlistlittleCover)
    }
}