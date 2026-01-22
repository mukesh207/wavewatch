package com.wavewatch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavewatch.data.model.AlertSeverity
import com.wavewatch.data.model.SecurityAlert
import com.wavewatch.data.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertsUiState(
    val isLoading: Boolean = true,
    val alerts: List<SecurityAlert> = emptyList(),
    val filteredAlerts: List<SecurityAlert> = emptyList(),
    val selectedFilter: AlertFilter = AlertFilter.ALL,
    val errorMessage: String? = null
)

enum class AlertFilter {
    ALL, HIGH, MEDIUM, LOW
}

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                val alerts = securityRepository.getSecurityAlerts()
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        alerts = alerts,
                        filteredAlerts = filterAlerts(alerts, state.selectedFilter)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Failed to load alerts: ${e.message}"
                )}
            }
        }
    }

    fun setFilter(filter: AlertFilter) {
        _uiState.update { state ->
            state.copy(
                selectedFilter = filter,
                filteredAlerts = filterAlerts(state.alerts, filter)
            )
        }
    }

    fun dismissAlert(alertId: String) {
        _uiState.update { state ->
            val id = alertId.toLongOrNull() ?: return@update state
            val updatedAlerts = state.alerts.filterNot { it.id == id }
            state.copy(
                alerts = updatedAlerts,
                filteredAlerts = filterAlerts(updatedAlerts, state.selectedFilter)
            )
        }
    }

    private fun filterAlerts(alerts: List<SecurityAlert>, filter: AlertFilter): List<SecurityAlert> {
        return when (filter) {
            AlertFilter.ALL -> alerts
            AlertFilter.HIGH -> alerts.filter { it.severity == AlertSeverity.HIGH }
            AlertFilter.MEDIUM -> alerts.filter { it.severity == AlertSeverity.MEDIUM }
            AlertFilter.LOW -> alerts.filter { it.severity == AlertSeverity.LOW }
        }
    }
}
