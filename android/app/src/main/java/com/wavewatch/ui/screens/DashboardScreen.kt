package com.wavewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wavewatch.data.model.*
import com.wavewatch.ui.theme.*
import com.wavewatch.ui.viewmodel.DashboardViewModel
import com.wavewatch.ui.viewmodel.DashboardUiState

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HeaderSection(uiState) }
        item { StatsSection(uiState) }
        item { ScanButton(isLoading = uiState.isLoading, onScan = viewModel::refresh) }
        
        if (!uiState.hasUsageStatsPermission) {
            item { PermissionBanner("Usage Stats permission required for app monitoring") }
        }
        
        item { TopAppsSection(uiState.topApps) }
        item { BluetoothSection(uiState.nearbyDevices, viewModel::trustDevice, viewModel::blockDevice) }
        
        uiState.errorMessage?.let { error ->
            item { ErrorBanner(error) }
        }
    }
}

@Composable
private fun HeaderSection(state: DashboardUiState) {
    val isSecure = (state.securityScore?.overallScore ?: 0) >= 70
    
    Column {
        Text(
            text = "🌊 WaveWatch",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isSecure) "Your device is secure ✓" else "⚠️ Security issues detected",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSecure) Success else Warning
        )
    }
}

@Composable
private fun StatsSection(state: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = state.securityScore?.overallScore?.toString() ?: "--",
            label = "Security Score",
            gradient = listOf(Primary, PrimaryLight)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = state.topApps.size.toString(),
            label = "Active Apps",
            gradient = listOf(DarkCard, DarkCard)
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    gradient: List<Color>
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun ScanButton(isLoading: Boolean, onScan: () -> Unit) {
    Button(
        onClick = onScan,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp),
        enabled = !isLoading
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(if (isLoading) "SCANNING..." else "SCAN NOW", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PermissionBanner(message: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Warning.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Warning)
            Spacer(Modifier.width(12.dp))
            Text(message, color = Warning, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Danger.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Danger)
            Spacer(Modifier.width(12.dp))
            Text(message, color = Danger, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun TopAppsSection(apps: List<AppUsageInfo>) {
    Column {
        Text(
            text = "📱 Top Data Users",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        
        if (apps.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No app data available", color = TextMuted)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    apps.take(5).forEach { app ->
                        AppListItem(app)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppListItem(app: AppUsageInfo) {
    val trustColor = when {
        app.trustScore >= 80 -> Success
        app.trustScore >= 50 -> Warning
        else -> Danger
    }
    
    val usageText = formatDataUsage(app.dataUsageBytes)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(app.appName.take(2).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.appName, fontWeight = FontWeight.Medium)
            Text(usageText, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(trustColor.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                app.trustScore.toString(),
                color = trustColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatDataUsage(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000 -> "%.1f GB".format(bytes / 1_000_000_000.0)
        bytes >= 1_000_000 -> "%.0f MB".format(bytes / 1_000_000.0)
        bytes >= 1_000 -> "%.0f KB".format(bytes / 1_000.0)
        else -> "$bytes B"
    }
}

@Composable
private fun BluetoothSection(
    devices: List<BluetoothDeviceInfo>,
    onTrust: (String) -> Unit,
    onBlock: (String) -> Unit
) {
    Column {
        Text(
            text = "📶 Bluetooth Nearby",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        
        if (devices.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Bluetooth devices found", color = TextMuted)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    devices.take(5).forEach { device ->
                        BluetoothItem(device, onTrust, onBlock)
                    }
                }
            }
        }
    }
}

@Composable
private fun BluetoothItem(
    device: BluetoothDeviceInfo,
    onTrust: (String) -> Unit,
    onBlock: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (device.isTrusted) Success else Warning)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(device.name ?: "Unknown Device", fontWeight = FontWeight.Medium)
            Text(
                if (device.isConnected) "Connected" else "Nearby",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
        Button(
            onClick = { 
                if (device.isTrusted) onBlock(device.address) else onTrust(device.address)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (device.isTrusted) Success.copy(alpha = 0.15f) else Danger
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                if (device.isTrusted) "Trusted" else "Block",
                color = if (device.isTrusted) Success else Color.White,
                fontSize = 11.sp
            )
        }
    }
}
