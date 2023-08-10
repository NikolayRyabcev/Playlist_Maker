package com.example.playlistmaker.UI.search.view_model.TrackAdapterAndViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.search.models.Track

class TrackAdapter(
    private val clickListener: TrackClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackLayoutBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(_items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_items[position])
        }

    }
    override fun getItemCount(): Int {
        return _items.size
    }

    fun interface TrackClick {
        fun onClick(track: Track)
    }

    fun setItems(items: List<Track>) {
        _items = items
    }
}