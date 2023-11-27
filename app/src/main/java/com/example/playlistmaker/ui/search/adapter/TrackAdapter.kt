package com.example.playlistmaker.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackLayoutBinding
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    private val clickListener: TrackClick,
    private val longClickListener : LongClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackLayoutBinding.inflate(layoutInspector, parent, false))
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(_items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_items[position])
            notifyDataSetChanged()
        }
        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick(_items[position])
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
    }
    override fun getItemCount(): Int {
        return _items.size
    }

    fun interface TrackClick {
        fun onClick(track: Track)
    }

    fun interface LongClick {
        fun onLongClick(track: Track)
    }

    fun setItems(items: List<Track>) {
        _items = items
        notifyDataSetChanged()
    }
}