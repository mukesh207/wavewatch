package com.wavewatch.data.source

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.wavewatch.data.model.BluetoothDeviceInfo
import com.wavewatch.data.model.DeviceType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for Bluetooth scanning and device management
 * Requires BLUETOOTH_SCAN and BLUETOOTH_CONNECT permissions on Android 12+
 */
@Singleton
class BluetoothDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    // Set of trusted device addresses (would normally be persisted)
    private val trustedDevices = mutableSetOf<String>()

    /**
     * Check if Bluetooth is available and enabled
     */
    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    /**
     * Check if we have the required Bluetooth permissions
     */
    fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Get list of currently paired/bonded devices
     */
    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDeviceInfo> {
        if (!hasBluetoothPermission()) return emptyList()
        
        return bluetoothAdapter?.bondedDevices?.map { device ->
            device.toBluetoothDeviceInfo(isConnected = false, rssi = 0)
        } ?: emptyList()
    }

    /**
     * Scan for nearby Bluetooth LE devices
     * Returns a Flow that emits discovered devices
     */
    @SuppressLint("MissingPermission")
    fun scanForDevices(): Flow<BluetoothDeviceInfo> = callbackFlow {
        if (!hasBluetoothPermission() || bluetoothAdapter == null) {
            close()
            return@callbackFlow
        }

        val scanner = bluetoothAdapter.bluetoothLeScanner
        val discoveredDevices = mutableSetOf<String>()

        // BLE Scan callback
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val address = result.device.address
                if (address !in discoveredDevices) {
                    discoveredDevices.add(address)
                    trySend(result.device.toBluetoothDeviceInfo(
                        isConnected = false,
                        rssi = result.rssi
                    ))
                }
            }

            override fun onScanFailed(errorCode: Int) {
                close(Exception("BLE scan failed with error: $errorCode"))
            }
        }

        // Classic Bluetooth discovery receiver
        val discoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                        val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                        
                        device?.let {
                            if (it.address !in discoveredDevices) {
                                discoveredDevices.add(it.address)
                                trySend(it.toBluetoothDeviceInfo(isConnected = false, rssi = rssi))
                            }
                        }
                    }
                }
            }
        }

        // Register receiver and start scanning
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)
        
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        
        scanner?.startScan(null, scanSettings, scanCallback)
        bluetoothAdapter.startDiscovery()

        awaitClose {
            scanner?.stopScan(scanCallback)
            bluetoothAdapter.cancelDiscovery()
            context.unregisterReceiver(discoveryReceiver)
        }
    }

    /**
     * Add a device to the trusted list
     */
    fun trustDevice(address: String) {
        trustedDevices.add(address)
    }

    /**
     * Remove a device from the trusted list (block it)
     */
    fun blockDevice(address: String) {
        trustedDevices.remove(address)
    }

    /**
     * Check if a device is trusted
     */
    fun isDeviceTrusted(address: String): Boolean = address in trustedDevices

    /**
     * Convert BluetoothDevice to our data model
     */
    @SuppressLint("MissingPermission")
    private fun BluetoothDevice.toBluetoothDeviceInfo(isConnected: Boolean, rssi: Int): BluetoothDeviceInfo {
        val deviceType = when (this.bluetoothClass?.majorDeviceClass) {
            0x0100 -> DeviceType.COMPUTER
            0x0200 -> DeviceType.PHONE
            0x0400 -> DeviceType.HEADPHONES
            0x0500 -> DeviceType.SPEAKER
            else -> {
                // Check name for hints
                val nameHint = name?.lowercase() ?: ""
                when {
                    "airpods" in nameHint || "buds" in nameHint || "headphone" in nameHint -> DeviceType.HEADPHONES
                    "watch" in nameHint || "band" in nameHint -> DeviceType.WATCH
                    "speaker" in nameHint -> DeviceType.SPEAKER
                    else -> DeviceType.UNKNOWN
                }
            }
        }

        return BluetoothDeviceInfo(
            address = address,
            name = name,
            rssi = rssi,
            deviceType = deviceType,
            isConnected = isConnected,
            isTrusted = isDeviceTrusted(address),
            lastSeenTimestamp = System.currentTimeMillis()
        )
    }
}
