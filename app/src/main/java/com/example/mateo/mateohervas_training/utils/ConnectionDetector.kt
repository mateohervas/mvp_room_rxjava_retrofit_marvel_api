package com.example.mateo.mateohervas_training.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectionDetector{

    fun isConnectingToInternet(context: Context): Boolean {

        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val info = connectivity.activeNetworkInfo

        if (info!=null && info.isConnected())
            return true
        return false
    }
}