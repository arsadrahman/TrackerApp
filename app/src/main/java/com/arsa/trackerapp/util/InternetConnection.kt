package com.arsa.trackerapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log


object InternetConnection {
    fun isConnected(context: Context): Boolean {
        var connected = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            e.message?.let { Log.e("Connectivity Exception", it) }
        }
        return connected
    }
}