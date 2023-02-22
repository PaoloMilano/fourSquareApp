package com.magicbluepenguin.utils.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

class NetworkChangeReceiver(private val context: Context) : BroadcastReceiver() {

    private var doOnNetworkAvailableAction: (() -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                val intentFilter = IntentFilter().apply {
                    addAction(ConnectivityManager.CONNECTIVITY_ACTION)
                }
                context.registerReceiver(this, intentFilter)
            } else {
                clear()
            }
        }

    fun doOnNetworkAvailable(action: (() -> Unit)? = null) {
        doOnNetworkAvailableAction = action
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (context.isNetworkAvailable()) {
            doOnNetworkAvailableAction?.invoke()
            doOnNetworkAvailableAction = null
        }
    }

    fun clear() {
        context.unregisterReceiver(this)
    }
}