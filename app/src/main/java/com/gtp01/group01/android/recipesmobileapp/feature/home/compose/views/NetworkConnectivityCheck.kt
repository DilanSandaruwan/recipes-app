package com.gtp01.group01.android.recipesmobileapp.feature.home.compose.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Composable function to monitor network connectivity changes and trigger actions accordingly.
 *
 * @param onRetry Callback function to be invoked when network connectivity is restored.
 */
@Composable
fun CheckNetworkConnectivity(
    onRetry: () -> Unit
) {
    // State to track network availability
    val networkAvailable = remember { mutableStateOf(true) }

    // Monitor network connectivity changes
    val localContext = LocalContext.current
    DisposableEffect(Unit) {
        val connectivityManager =
            localContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkAvailable.value = true
                // Trigger auto-refresh
                onRetry()
            }

            override fun onLost(network: Network) {
                networkAvailable.value = false
            }
        }
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Unregister callback when the composable is disposed
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}