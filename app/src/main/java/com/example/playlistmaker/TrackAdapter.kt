package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    var tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {
    var text = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        val searchHistoryObj = SearchHistory()

        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistoryObj.editArray(tracks[position])
            text= "added"

            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(holder.itemView.context, text, duration)
            toast.show()
            text = ""
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}