package com.magicbluepenguin.utils.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class NetworkChangeReceiver : BroadcastReceiver() {

    var onNetworkAvailableListener: (() -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent?) {
        if (context.isNetworkAvailable()) {
            onNetworkAvailableListener?.invoke()
        }
    }
}