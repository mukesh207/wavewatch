package com.wavewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wavewatch.data.model.AlertSeverity
import com.wavewatch.data.model.SecurityAlert
import com.wavewatch.ui.theme.*
import com.wavewatch.ui.viewmodel.AlertFilter
import com.wavewatch.ui.viewmodel.AlertsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🔔 Alerts",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            val highCount = uiState.alerts.count { it.severity == AlertSeverity.HIGH }
            Text(
                text = if (highCount > 0) "$highCount critical alerts need attention" else "All clear!",
                style = MaterialTheme.typography.bodyMedium,
                color = if (highCount > 0) Danger else Success
            )
        }

        // Alert Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.selectedFilter == AlertFilter.ALL,
                onClick = { viewModel.setFilter(AlertFilter.ALL) },
                label = { Text("All (${uiState.alerts.size})") }
            )
            FilterChip(
                selected = uiState.selectedFilter == AlertFilter.HIGH,
                onClick = { viewModel.setFilter(AlertFilter.HIGH) },
                label = { Text("Critical") }
            )
            FilterChip(
                selected = uiState.selectedFilter == AlertFilter.MEDIUM,
                onClick = { viewModel.setFilter(AlertFilter.MEDIUM) },
                label = { Text("Warning") }
            )
            FilterChip(
                selected = uiState.selectedFilter == AlertFilter.LOW,
                onClick = { viewModel.setFilter(AlertFilter.LOW) },
                label = { Text("Info") }
            )
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading alerts...", color = TextMuted)
            }
        } else if (uiState.filteredAlerts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✅", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("No alerts", fontWeight = FontWeight.SemiBold)
                    Text("Everything looks good!", color = TextMuted)
                }
            }
        } else {
            // Alerts List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredAlerts, key = { it.id }) { alert ->
                    AlertCard(
                        alert = alert,
                        onDismiss = { viewModel.dismissAlert(alert.id.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertCard(alert: SecurityAlert, onDismiss: () -> Unit) {
    val (icon, color) = when (alert.severity) {
        AlertSeverity.HIGH, AlertSeverity.CRITICAL -> Icons.Default.Warning to Danger
        AlertSeverity.MEDIUM -> Icons.Default.Info to Warning
        AlertSeverity.LOW -> Icons.Default.CheckCircle to Success
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formatTime(alert.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = alert.category.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* TODO: View details */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("View", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = onDismiss,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Dismiss", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} min ago"
        diff < 86400000 -> "${diff / 3600000} hours ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}
