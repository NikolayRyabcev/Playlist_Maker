package com.example.playlistmaker

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(
    var tracks: ArrayList<Track>,
    private val clickListener:TrackClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener.onClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClick {
        fun onClick(track:Track)
    }

}