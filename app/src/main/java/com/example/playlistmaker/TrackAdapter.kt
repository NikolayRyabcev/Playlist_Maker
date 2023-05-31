package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    var tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        var trackHistoryList: ArrayList<Long> = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val searchHistoryObj = SearchHistory()
        val searchActivityObj = SearchActivity()
        val historyTrackId = holder.itemId
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistoryObj.editArray(historyTrackId)
            searchHistoryObj.saveHistory(searchActivityObj.savedHistory)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}