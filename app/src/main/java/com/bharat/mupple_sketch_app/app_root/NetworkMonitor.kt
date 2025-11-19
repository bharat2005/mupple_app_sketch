package com.bharat.mupple_sketch_app.app_root


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(isNetworkAvailable(context))
     val isOnline = _isOnline.asStateFlow()

    fun isCurrentlyConnected() : Boolean = connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: android.net.Network) {
            _isOnline.value = true
        }

        override fun onLost(network: Network) {
            _isOnline.value = false
        }
    }


    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    fun unregister(){
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun isNetworkAvailable(context : Context) : Boolean {
        val connetivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connetivityManager.activeNetwork ?: return false
        val capabilities = connetivityManager.getNetworkCapabilities(network) ?: return false

        val hasCapabilities = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        return hasCapabilities
    }




}