package com.example.playlistmaker.UI.search.view_model.TrackAdapterAndViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class TrackAdapter(
    var tracks: ArrayList<Track>,
    private val clickListener: TrackClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getTrackSharedPreferences()
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener.onClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClick {
        fun onClick(track: Track)
    }

}