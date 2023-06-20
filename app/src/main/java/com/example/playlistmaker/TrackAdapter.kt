package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(
    var tracks: ArrayList<Track>
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
            notifyDataSetChanged()

            val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
            intent.putExtra("Track Name", tracks[position].trackName)
            intent.putExtra("Artist Name", tracks[position].artistName)
            val trackTime= SimpleDateFormat("mm:ss", Locale.getDefault()).format(tracks[position].trackTimeMillis)
            intent.putExtra("Track Time", trackTime)
            intent.putExtra("Album", tracks[position].collectionName)
            intent.putExtra("Year", tracks[position].releaseDate)
            intent.putExtra("Genre", tracks[position].primaryGenreName)
            intent.putExtra("Country", tracks[position].country)
            intent.putExtra("Cover", tracks[position].artworkUrl100)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}