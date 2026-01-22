package com.wavewatch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavewatch.data.model.*
import com.wavewatch.data.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val securityScore: SecurityScore? = null,
    val topApps: List<AppUsageInfo> = emptyList(),
    val networkInfo: NetworkInfo? = null,
    val nearbyDevices: List<BluetoothDeviceInfo> = emptyList(),
    val hasUsageStatsPermission: Boolean = false,
    val hasBluetoothPermission: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        checkPermissions()
        loadDashboardData()
        observeNetwork()
    }

    private fun checkPermissions() {
        _uiState.update { it.copy(
            hasUsageStatsPermission = securityRepository.hasUsageStatsPermission(),
            hasBluetoothPermission = securityRepository.hasBluetoothPermission()
        )}
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                val score = securityRepository.getSecurityScore()
                val apps = securityRepository.getTopDataApps(5)
                val devices = securityRepository.getPairedDevices()
                val network = securityRepository.getNetworkInfo()
                
                _uiState.update { it.copy(
                    isLoading = false,
                    securityScore = score,
                    topApps = apps,
                    nearbyDevices = devices,
                    networkInfo = network
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Failed to load data: ${e.message}"
                )}
            }
        }
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            securityRepository.observeNetwork().collect { networkInfo ->
                _uiState.update { it.copy(networkInfo = networkInfo) }
            }
        }
    }

    fun scanBluetooth() {
        if (!securityRepository.hasBluetoothPermission()) {
            _uiState.update { it.copy(errorMessage = "Bluetooth permission required") }
            return
        }
        
        viewModelScope.launch {
            val devices = mutableListOf<BluetoothDeviceInfo>()
            securityRepository.scanBluetoothDevices()
                .take(10)
                .collect { device ->
                    devices.add(device)
                    _uiState.update { it.copy(nearbyDevices = devices.toList()) }
                }
        }
    }

    fun trustDevice(address: String) {
        securityRepository.trustBluetoothDevice(address)
        _uiState.update { state ->
            state.copy(nearbyDevices = state.nearbyDevices.map { device ->
                if (device.address == address) device.copy(isTrusted = true) else device
            })
        }
    }

    fun blockDevice(address: String) {
        securityRepository.blockBluetoothDevice(address)
        _uiState.update { state ->
            state.copy(nearbyDevices = state.nearbyDevices.map { device ->
                if (device.address == address) device.copy(isTrusted = false) else device
            })
        }
    }

    fun refresh() {
        checkPermissions()
        loadDashboardData()
    }
}
