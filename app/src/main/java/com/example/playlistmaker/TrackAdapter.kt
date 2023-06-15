package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    var tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {
    private val searchHistoryObj = SearchHistory()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistoryObj.editArray(tracks[position])
            val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
            holder.itemView.context.startActivity(intent)
           // searchHistoryObj.toaster(holder.itemView.context,searchHistoryObj.counter.toString() )
           // startActivity(Intent(holder.itemView.context, PlayerActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}