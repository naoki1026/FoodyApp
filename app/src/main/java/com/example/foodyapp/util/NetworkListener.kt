package com.example.foodyapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener : ConnectivityManager.NetworkCallback(){

    // 状態保持のための特別なSharedFlowで、MutableStateFlowかstateInで作成できる
    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context) : MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false
        connectivityManager.allNetworks.forEach {  network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if(it.hasCapability((NetworkCapabilities.NET_CAPABILITY_INTERNET))) {
                    isConnected = true
                    return@forEach
                }
            }
        }
        isNetworkAvailable.value = isConnected
        return isNetworkAvailable
    }

    // Control + Oを押すことでメソッドが表示される
    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}