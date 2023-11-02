package com.example.playlistmaker.ui.mediaLibrary.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLayoutBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistlittleName.text = item.playlistName
        val number= item.arrayNumber.toString() + " треков"
        binding.playlistlittleSongNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)

        if (item.uri.isNullOrEmpty()) {
            val imageResource = R.drawable.musicalbum
            binding.playlistlittleCover.setImageResource(imageResource)
            val layoutParams = binding.playlistlittleCover.layoutParams
            layoutParams.width = 104
            layoutParams.height = 103
            binding.playlistlittleCover.layoutParams = layoutParams
        } else {

            Glide.with(itemView)
                .load(item.uri)
                .placeholder(R.drawable.musicalbum)
                .transform(RoundedCorners(radius))
                .into(binding.playlistlittleCover)
        }
    }
}