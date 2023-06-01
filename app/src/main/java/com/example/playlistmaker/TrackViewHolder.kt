package com.example.playlistmaker

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val groupName: TextView = itemView.findViewById(R.id.groupName)
    val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(item: Track) {
        val adaptedTrackName = item.trackName
        val adaptedArtistName = item.artistName
        var trackNumber = item.trackId


        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.musicalbum)
            .into(trackImage)

        trackName.text = adaptedTrackName
        groupName.text = adaptedArtistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
    }




}
