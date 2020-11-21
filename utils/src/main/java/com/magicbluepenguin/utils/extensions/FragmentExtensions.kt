package com.magicbluepenguin.utils.extensions

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

fun Fragment.setSupportActionBar(toolbar: Toolbar): ActionBar = (activity as AppCompatActivity).let {
    it.setSupportActionBar(toolbar)
    requireNotNull(it.supportActionBar)
}

fun Fragment.doOnNetworkAvailable(action: () -> Unit) {
    val networkChangeReceiver = NetworkChangeReceiver().apply {
        onNetworkAvailableListener = {
            action.invoke()
            onNetworkAvailableListener = null
            requireContext().unregisterReceiver(this)
        }
    }
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            networkChangeReceiver.onNetworkAvailableListener = null
            requireContext().unregisterReceiver(networkChangeReceiver)
        }
    })

    val intentFilter = IntentFilter().apply {
        addAction(ConnectivityManager.CONNECTIVITY_ACTION)
    }
    requireContext().registerReceiver(networkChangeReceiver, intentFilter)
}
