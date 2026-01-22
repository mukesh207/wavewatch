package com.wavewatch.data.source

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import com.wavewatch.data.model.ConnectionType
import com.wavewatch.data.model.NetworkInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for network connectivity monitoring
 */
@Singleton
class NetworkDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

    /**
     * Get current network connection info
     */
    fun getCurrentNetworkInfo(): NetworkInfo {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        if (capabilities == null) {
            return NetworkInfo(
                isConnected = false,
                connectionType = ConnectionType.NONE,
                networkName = null,
                isSecure = false,
                signalStrength = 0,
                ipAddress = null
            )
        }

        val connectionType = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
            else -> ConnectionType.NONE
        }

        val (networkName, signalStrength, isSecure) = when (connectionType) {
            ConnectionType.WIFI -> getWifiDetails()
            ConnectionType.CELLULAR -> Triple("Mobile Data", getCellularSignalStrength(capabilities), true)
            ConnectionType.VPN -> Triple("VPN", 100, true)
            else -> Triple(null, 0, false)
        }

        return NetworkInfo(
            isConnected = true,
            connectionType = connectionType,
            networkName = networkName,
            isSecure = isSecure,
            signalStrength = signalStrength,
            ipAddress = getLocalIpAddress()
        )
    }

    /**
     * Observe network changes as a Flow
     */
    fun observeNetworkChanges(): Flow<NetworkInfo> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(getCurrentNetworkInfo())
            }

            override fun onLost(network: Network) {
                trySend(NetworkInfo(
                    isConnected = false,
                    connectionType = ConnectionType.NONE,
                    networkName = null,
                    isSecure = false,
                    signalStrength = 0,
                    ipAddress = null
                ))
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(getCurrentNetworkInfo())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit initial state
        trySend(getCurrentNetworkInfo())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    /**
     * Check if current network is potentially unsafe
     */
    fun isNetworkSecure(): Boolean {
        val info = getCurrentNetworkInfo()
        return info.isSecure || info.connectionType == ConnectionType.CELLULAR
    }

    /**
     * Get WiFi details including SSID, signal strength, and security
     */
    @Suppress("DEPRECATION")
    private fun getWifiDetails(): Triple<String?, Int, Boolean> {
        val wifiInfo: WifiInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // On Android 10+, we need location permission for SSID
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.transportInfo as? WifiInfo
        } else {
            wifiManager?.connectionInfo
        }

        if (wifiInfo == null) {
            return Triple(null, 0, false)
        }

        val ssid = wifiInfo.ssid?.replace("\"", "")?.takeIf { it != "<unknown ssid>" }
        
        // Convert RSSI to percentage (typical range: -100 to -30 dBm)
        val signalStrength = when {
            wifiInfo.rssi >= -50 -> 100
            wifiInfo.rssi >= -60 -> 80
            wifiInfo.rssi >= -70 -> 60
            wifiInfo.rssi >= -80 -> 40
            wifiInfo.rssi >= -90 -> 20
            else -> 10
        }

        // Check if network uses security (this is a simplified check)
        // In real app, you'd check the network's security type
        val isSecure = true // Assume secured; detailed check requires more permissions

        return Triple(ssid, signalStrength, isSecure)
    }

    /**
     * Get cellular signal strength from network capabilities
     */
    private fun getCellularSignalStrength(capabilities: NetworkCapabilities): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            capabilities.signalStrength.let { strength ->
                // Convert dBm to percentage (typical range: -120 to -50 dBm)
                when {
                    strength >= -70 -> 100
                    strength >= -85 -> 75
                    strength >= -100 -> 50
                    strength >= -110 -> 25
                    else -> 10
                }
            }
        } else {
            50 // Default for older devices
        }
    }

    /**
     * Get local IP address
     */
    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is Inet4Address) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore
        }
        return null
    }
}
