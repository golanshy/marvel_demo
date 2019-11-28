package uk.co.applylogic.marvel.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkUtils {

    companion object {

        fun isConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                        ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ) {
                postAndroidMInternetCheck(connectivityManager)
            } else {
                preAndroidMInternetCheck(connectivityManager)
            }
        }

        @Suppress("DEPRECATION")
        private fun preAndroidMInternetCheck(
            connectivityManager: ConnectivityManager
        ): Boolean {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun postAndroidMInternetCheck(
            connectivityManager: ConnectivityManager
        ): Boolean {
            val network = connectivityManager.activeNetwork
            val connection =
                connectivityManager.getNetworkCapabilities(network)

            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }
    }
}