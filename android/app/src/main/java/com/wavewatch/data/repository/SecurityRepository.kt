package com.wavewatch.data.repository

import com.wavewatch.data.model.*
import com.wavewatch.data.source.AppUsageDataSource
import com.wavewatch.data.source.BluetoothDataSource
import com.wavewatch.data.source.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for security-related data
 */
@Singleton
class SecurityRepository @Inject constructor(
    private val appUsageDataSource: AppUsageDataSource,
    private val bluetoothDataSource: BluetoothDataSource,
    private val networkDataSource: NetworkDataSource,
    private val aiAssistantRepository: AIAssistantRepository
) {
    /**
     * Calculate overall security score
     */
    suspend fun getSecurityScore(): SecurityScore {
        val networkInfo = networkDataSource.getCurrentNetworkInfo()
        val appStats = appUsageDataSource.getAppUsageStats(7)
        
        // Network score: based on connection security
        val networkScore = when {
            !networkInfo.isConnected -> 100
            networkInfo.isSecure -> 90
            networkInfo.connectionType == ConnectionType.CELLULAR -> 85
            else -> 50
        }
        
        // App score: average trust score of top apps
        val appScore = if (appStats.isNotEmpty()) {
            appStats.take(10).map { it.trustScore }.average().toInt()
        } else {
            80
        }
        
        // Bluetooth score: based on paired devices
        val pairedDevices = bluetoothDataSource.getPairedDevices()
        val trustedCount = pairedDevices.count { it.isTrusted }
        val bluetoothScore = when {
            pairedDevices.isEmpty() -> 100
            trustedCount == pairedDevices.size -> 95
            trustedCount > pairedDevices.size / 2 -> 75
            else -> 50
        }
        
        // Overall score: weighted average
        val overallScore = (networkScore * 0.35 + appScore * 0.40 + bluetoothScore * 0.25).toInt()
        
        return SecurityScore(
            overallScore = overallScore,
            networkScore = networkScore,
            appScore = appScore,
            bluetoothScore = bluetoothScore,
            lastUpdated = System.currentTimeMillis()
        )
    }
    
    /**
     * Get top data-consuming apps
     */
    suspend fun getTopDataApps(limit: Int = 10): List<AppUsageInfo> {
        return appUsageDataSource.getAppUsageStats()
            .sortedByDescending { it.dataUsageBytes }
            .take(limit)
    }
    
    /**
     * Get current network status
     */
    fun getNetworkInfo(): NetworkInfo = networkDataSource.getCurrentNetworkInfo()
    
    /**
     * Observe network changes
     */
    fun observeNetwork(): Flow<NetworkInfo> = networkDataSource.observeNetworkChanges()
    
    /**
     * Get paired Bluetooth devices
     */
    fun getPairedDevices(): List<BluetoothDeviceInfo> = bluetoothDataSource.getPairedDevices()
    
    /**
     * Scan for nearby Bluetooth devices
     */
    fun scanBluetoothDevices(): Flow<BluetoothDeviceInfo> = bluetoothDataSource.scanForDevices()
    
    /**
     * Trust a Bluetooth device
     */
    fun trustBluetoothDevice(address: String) = bluetoothDataSource.trustDevice(address)
    
    /**
     * Block a Bluetooth device
     */
    fun blockBluetoothDevice(address: String) = bluetoothDataSource.blockDevice(address)
    
    /**
     * Check if usage stats permission is granted
     */
    fun hasUsageStatsPermission(): Boolean = appUsageDataSource.hasUsageStatsPermission()
    
    /**
     * Check if Bluetooth permission is granted
     */
    fun hasBluetoothPermission(): Boolean = bluetoothDataSource.hasBluetoothPermission()

    /**
     * Get security alerts based on current state
     */
    suspend fun getSecurityAlerts(): List<SecurityAlert> {
        val alerts = mutableListOf<SecurityAlert>()
        var alertId = 1L
        
        // Network alerts
        val networkInfo = networkDataSource.getCurrentNetworkInfo()
        if (networkInfo.isConnected && !networkInfo.isSecure) {
            alerts.add(SecurityAlert(
                id = alertId++,
                title = "Insecure Network",
                description = "You're connected to an unsecured WiFi network. Your data may be visible to others.",
                severity = AlertSeverity.HIGH,
                timestamp = System.currentTimeMillis(),
                category = AlertCategory.NETWORK
            ))
        }
        
        // App alerts
        val apps = appUsageDataSource.getAppUsageStats(7)
        val lowTrustApps = apps.filter { it.trustScore < 50 }
        lowTrustApps.take(3).forEach { app ->
            alerts.add(SecurityAlert(
                id = alertId++,
                title = "Low Trust App: ${app.appName}",
                description = "This app has a low trust score (${app.trustScore}/100). Review its permissions.",
                severity = AlertSeverity.MEDIUM,
                timestamp = System.currentTimeMillis() - (1..24).random() * 3600000L,
                category = AlertCategory.APP_PERMISSION
            ))
        }
        
        // High data usage alerts
        val highDataApps = apps.filter { it.dataUsageBytes > 500 * 1024 * 1024 }
        highDataApps.take(2).forEach { app ->
            val usedMb = app.dataUsageBytes / (1024 * 1024)
            alerts.add(SecurityAlert(
                id = alertId++,
                title = "High Data Usage: ${app.appName}",
                description = "This app has used ${usedMb}MB in the last 7 days.",
                severity = AlertSeverity.LOW,
                timestamp = System.currentTimeMillis() - (1..48).random() * 3600000L,
                category = AlertCategory.DATA_USAGE
            ))
        }
        
        // Bluetooth alerts
        val devices = bluetoothDataSource.getPairedDevices()
        val untrustedDevices = devices.filter { !it.isTrusted }
        if (untrustedDevices.isNotEmpty()) {
            alerts.add(SecurityAlert(
                id = alertId++,
                title = "${untrustedDevices.size} Untrusted Devices",
                description = "You have ${untrustedDevices.size} paired Bluetooth devices that haven't been marked as trusted.",
                severity = AlertSeverity.LOW,
                timestamp = System.currentTimeMillis() - 7200000L,
                category = AlertCategory.BLUETOOTH
            ))
        }
        
        // Enhance alerts with AI context
        val enhancedAlerts = alerts.map { aiAssistantRepository.enhanceAlert(it) }
        
        return enhancedAlerts.sortedByDescending { it.timestamp }
    }
}

