package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackImage : ImageView = itemView.findViewById(R.id.trackImage)
    val trackName : TextView = itemView.findViewById(R.id.trackName)
    val groupName : TextView = itemView.findViewById(R.id.groupName)
    val trackTime : TextView = itemView.findViewById(R.id.trackTime)

    fun bind (item:Track) {
        val adaptedTrackName = item.trackName
        val adaptedArtistName = item.artistName
        val adaptedTrackTime = item.trackTime
        val adaptedTrackPicture = item.artworkUrl100

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .into(trackImage)


        trackName.text=adaptedTrackName
        groupName.text=adaptedArtistName
        trackTime.text=adaptedTrackTime
    }
}