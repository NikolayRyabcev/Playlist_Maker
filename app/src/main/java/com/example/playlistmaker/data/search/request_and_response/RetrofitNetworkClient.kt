package com.example.playlistmaker.data.search.request_and_response

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class RetrofitNetworkClient(
    private val iTunesService: iTunesSearchAPI,
    private val context: Context
) :
    NetworkClient {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return if (dto is TrackSearchRequest) {
            val resp = try {
                iTunesService.search(dto.expression).execute()
            } catch(ex:Exception) {
                null
            }
            val body = resp?.body() ?: Response()
            body.apply {
                if (resp != null) {
                    resultCode = resp.code()
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}