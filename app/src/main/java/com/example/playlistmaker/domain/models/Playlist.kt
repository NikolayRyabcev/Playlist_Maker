package com.example.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist (
    val playlistName:String,
    val description:String?,
    val uri:String,
    val trackArray:List<Track>,
    val arrayNumber:Int?,
) : Parcelable