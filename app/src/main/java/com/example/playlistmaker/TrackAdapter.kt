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
    var tracks: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchActivityObj = SearchActivity()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        isClickAllowed = true
        holder.bind(tracks[position])
        if (clickDebounce()) {
            holder.itemView.setOnClickListener {

                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra("Track Name", tracks[position].trackName)
                intent.putExtra("Artist Name", tracks[position].artistName)
                val trackTime = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(tracks[position].trackTimeMillis)
                intent.putExtra("Track Time", trackTime)
                intent.putExtra("Album", tracks[position].collectionName)
                intent.putExtra("Year", tracks[position].releaseDate)
                intent.putExtra("Genre", tracks[position].primaryGenreName)
                intent.putExtra("Country", tracks[position].country)
                intent.putExtra("Cover", tracks[position].artworkUrl100)
                intent.putExtra("URL", tracks[position].previewUrl)
                holder.itemView.context.startActivity(intent)

                searchActivityObj.searchHistoryObj.editArray(tracks[position])
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}