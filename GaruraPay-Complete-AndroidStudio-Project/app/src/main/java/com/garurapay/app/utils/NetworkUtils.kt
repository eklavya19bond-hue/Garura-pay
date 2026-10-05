package com.garurapay.app.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    /**
     * Checks whether an active internet connection is available.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    /**
     * Requirement 1:
     * If there is no internet, display a blocking dialog saying:
     * "No Internet Connection. Please connect to the internet to use Garura Pay."
     */
    fun checkAndEnforceNetwork(activity: Activity, onConnected: (() -> Unit)? = null) {
        if (!isNetworkAvailable(activity)) {
            AlertDialog.Builder(activity)
                .setTitle("Network Required")
                .setMessage("No Internet Connection. Please connect to the internet to use Garura Pay.")
                .setCancelable(false)
                .setPositiveButton("Retry") { d, _ ->
                    d.dismiss()
                    checkAndEnforceNetwork(activity, onConnected)
                }
                .show()
        } else {
            onConnected?.invoke()
        }
    }
}
