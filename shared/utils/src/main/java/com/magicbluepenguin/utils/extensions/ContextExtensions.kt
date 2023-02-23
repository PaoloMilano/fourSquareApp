package com.magicbluepenguin.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

fun Context.isNetworkAvailable(): Boolean {
    val connManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null
    } else {
        val activeNetwork = connManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true && activeNetwork.isAvailable
    }
}