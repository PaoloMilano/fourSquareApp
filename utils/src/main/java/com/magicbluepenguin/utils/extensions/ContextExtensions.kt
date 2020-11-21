package com.magicbluepenguin.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager


@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnected == true
}
