package com.example.playlistmaker.App

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.playlistmaker.data.search.requestAndResponse.InternetConnectionIndicator

class ConnectionBroadcastReciever : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        val internetConnectionIndicator = context?.let { InternetConnectionIndicator(it) }
        val isEnabled = internetConnectionIndicator?.isConnected()
        if (!isEnabled!!) {
            Toast.makeText(context, "Отсутствует подключение к интернету", Toast.LENGTH_LONG).show()
        }
    }
}