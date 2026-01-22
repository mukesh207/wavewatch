package com.wavewatch.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class SettingsUiState(
    val isLoading: Boolean = true,
    val nightWatchEnabled: Boolean = true,
    val bluetoothMonitoringEnabled: Boolean = true,
    val networkAlertsEnabled: Boolean = true,
    val dataUsageAlertsEnabled: Boolean = false,
    val biometricLockEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    companion object {
        val NIGHT_WATCH_ENABLED = booleanPreferencesKey("night_watch_enabled")
        val BLUETOOTH_MONITORING = booleanPreferencesKey("bluetooth_monitoring")
        val NETWORK_ALERTS = booleanPreferencesKey("network_alerts")
        val DATA_USAGE_ALERTS = booleanPreferencesKey("data_usage_alerts")
        val BIOMETRIC_LOCK = booleanPreferencesKey("biometric_lock")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
    }

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val prefs = context.dataStore.data.first()
                _uiState.update { it.copy(
                    isLoading = false,
                    nightWatchEnabled = prefs[NIGHT_WATCH_ENABLED] ?: true,
                    bluetoothMonitoringEnabled = prefs[BLUETOOTH_MONITORING] ?: true,
                    networkAlertsEnabled = prefs[NETWORK_ALERTS] ?: true,
                    dataUsageAlertsEnabled = prefs[DATA_USAGE_ALERTS] ?: false,
                    biometricLockEnabled = prefs[BIOMETRIC_LOCK] ?: false,
                    notificationsEnabled = prefs[NOTIFICATIONS] ?: true
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setNightWatchEnabled(enabled: Boolean) {
        updateSetting(NIGHT_WATCH_ENABLED, enabled) {
            _uiState.update { it.copy(nightWatchEnabled = enabled) }
        }
    }

    fun setBluetoothMonitoringEnabled(enabled: Boolean) {
        updateSetting(BLUETOOTH_MONITORING, enabled) {
            _uiState.update { it.copy(bluetoothMonitoringEnabled = enabled) }
        }
    }

    fun setNetworkAlertsEnabled(enabled: Boolean) {
        updateSetting(NETWORK_ALERTS, enabled) {
            _uiState.update { it.copy(networkAlertsEnabled = enabled) }
        }
    }

    fun setDataUsageAlertsEnabled(enabled: Boolean) {
        updateSetting(DATA_USAGE_ALERTS, enabled) {
            _uiState.update { it.copy(dataUsageAlertsEnabled = enabled) }
        }
    }

    fun setBiometricLockEnabled(enabled: Boolean) {
        updateSetting(BIOMETRIC_LOCK, enabled) {
            _uiState.update { it.copy(biometricLockEnabled = enabled) }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        updateSetting(NOTIFICATIONS, enabled) {
            _uiState.update { it.copy(notificationsEnabled = enabled) }
        }
    }

    private fun updateSetting(
        key: Preferences.Key<Boolean>,
        value: Boolean,
        onUpdate: () -> Unit
    ) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[key] = value
            }
            onUpdate()
        }
    }
}
