package com.example.playlistmaker.ui.player.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistTrackerBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetViewHolder(
    private val binding: PlaylistTrackerBinding
) :RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.PlaylistName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val text = when {
            innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11 -> " трек"
            innerNumber.toInt() % 10 == 2 && innerNumber.toInt() % 100 != 12 -> " трека"
            innerNumber.toInt() % 10 == 3 && innerNumber.toInt() % 100 != 13 -> " трека"
            innerNumber.toInt() % 10 == 4 && innerNumber.toInt() % 100 != 14 -> " трека"
            else -> " треков"
        }
        val number = "$innerNumber $text"
        binding.playlistNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)
        val width = 45
        val height = 45
        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.musicalbum)
            .transform(CenterCrop(), RoundedCorners(radius))
            .override(width, height)
            .into(binding.playlistImage)
    }
}