package com.example.playlistmaker.presentation.ui.TrackAdapterAndViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val groupName: TextView = itemView.findViewById(R.id.groupName)
    val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    var trackNumber:Long = 0


    fun bind(item: Track) {
        val adaptedTrackName = item.trackName
        val adaptedArtistName = item.artistName
        trackNumber = item.trackId
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.musicalbum)
            .into(trackImage)

        trackName.text = adaptedTrackName
        groupName.text = adaptedArtistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
    }

}
