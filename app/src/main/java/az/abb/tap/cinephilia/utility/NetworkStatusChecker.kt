package az.abb.tap.cinephilia.utility

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*

class NetworkStatusChecker(private val connectivityManager: ConnectivityManager?) {

    fun hasInternetConnection(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}