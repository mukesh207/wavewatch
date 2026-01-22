package com.wavewatch.data.model

/**
 * Represents an installed app with its usage statistics
 */
data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val dataUsageBytes: Long,
    val lastUsedTimestamp: Long,
    val permissionCount: Int,
    val trustScore: Int // 0-100
)

/**
 * Represents a detected Bluetooth device
 */
data class BluetoothDeviceInfo(
    val address: String,
    val name: String?,
    val rssi: Int,
    val deviceType: DeviceType,
    val isConnected: Boolean,
    val isTrusted: Boolean,
    val lastSeenTimestamp: Long
)

enum class DeviceType {
    PHONE, COMPUTER, HEADPHONES, SPEAKER, WATCH, UNKNOWN
}

/**
 * Represents the current network connection status
 */
data class NetworkInfo(
    val isConnected: Boolean,
    val connectionType: ConnectionType,
    val networkName: String?,
    val isSecure: Boolean,
    val signalStrength: Int, // 0-100
    val ipAddress: String?
)

enum class ConnectionType {
    WIFI, CELLULAR, ETHERNET, VPN, NONE
}

/**
 * Represents a security alert
 */
data class SecurityAlert(
    val id: Long,
    val title: String,
    val description: String,
    val severity: AlertSeverity,
    val timestamp: Long,
    val isRead: Boolean = false,
    val category: AlertCategory
)

enum class AlertSeverity { LOW, MEDIUM, HIGH, CRITICAL }

enum class AlertCategory {
    NETWORK, BLUETOOTH, APP_PERMISSION, DATA_USAGE, SYSTEM
}

/**
 * Overall security score with breakdown
 */
data class SecurityScore(
    val overallScore: Int, // 0-100
    val networkScore: Int,
    val appScore: Int,
    val bluetoothScore: Int,
    val lastUpdated: Long
)
