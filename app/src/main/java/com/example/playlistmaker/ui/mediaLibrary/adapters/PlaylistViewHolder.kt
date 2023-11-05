package com.example.playlistmaker.ui.mediaLibrary.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistlittleName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val number= innerNumber + " треков"
        binding.playlistlittleSongNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)

        if (item.uri.isEmpty()) {
            val imageResource = R.drawable.musicalbum
            binding.playlistlittleCover.setImageResource(imageResource)
            val layoutParams = binding.playlistlittleCover.layoutParams
            layoutParams.width = 104
            layoutParams.height = 103
            binding.playlistlittleCover.layoutParams = layoutParams
        } else {

            val width = 160
            val height = 160
            Glide.with(itemView)
                .load(item.uri)
                .placeholder(R.drawable.musicalbum)
                .transform(CenterCrop(), RoundedCorners(radius))
                .override(width, height)
                .into(binding.playlistlittleCover)
        }
    }
}