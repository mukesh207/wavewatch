package com.wavewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wavewatch.ui.theme.*
import com.wavewatch.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SettingsHeader() }
        
        item {
            SettingsSection("Protection") {
                SettingsToggleItem(
                    icon = Icons.Default.NightsStay,
                    title = "Night Watch",
                    description = "Monitor apps while you sleep",
                    checked = uiState.nightWatchEnabled,
                    onCheckedChange = viewModel::setNightWatchEnabled
                )
                SettingsToggleItem(
                    icon = Icons.Default.Bluetooth,
                    title = "Bluetooth Monitoring",
                    description = "Detect unknown Bluetooth devices",
                    checked = uiState.bluetoothMonitoringEnabled,
                    onCheckedChange = viewModel::setBluetoothMonitoringEnabled
                )
                SettingsToggleItem(
                    icon = Icons.Default.Wifi,
                    title = "Network Alerts",
                    description = "Alert on insecure connections",
                    checked = uiState.networkAlertsEnabled,
                    onCheckedChange = viewModel::setNetworkAlertsEnabled
                )
            }
        }

        item {
            SettingsSection("Notifications") {
                SettingsToggleItem(
                    icon = Icons.Default.Notifications,
                    title = "Push Notifications",
                    description = "Receive security alerts",
                    checked = uiState.notificationsEnabled,
                    onCheckedChange = viewModel::setNotificationsEnabled
                )
                SettingsToggleItem(
                    icon = Icons.Default.DataUsage,
                    title = "Data Usage Alerts",
                    description = "Alert when apps use excessive data",
                    checked = uiState.dataUsageAlertsEnabled,
                    onCheckedChange = viewModel::setDataUsageAlertsEnabled
                )
            }
        }

        item {
            SettingsSection("Security") {
                SettingsToggleItem(
                    icon = Icons.Default.Fingerprint,
                    title = "Biometric Lock",
                    description = "Require fingerprint to open app",
                    checked = uiState.biometricLockEnabled,
                    onCheckedChange = viewModel::setBiometricLockEnabled,
                    accentColor = Accent
                )
            }
        }

        item {
            SettingsSection("About") {
                SettingsNavigationItem(
                    icon = Icons.Default.Info,
                    title = "App Version",
                    description = "1.0.0 (Beta)"
                )
                SettingsNavigationItem(
                    icon = Icons.Default.Policy,
                    title = "Privacy Policy",
                    description = "View our privacy policy"
                )
                SettingsNavigationItem(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    description = "View terms and conditions"
                )
            }
        }
    }
}

@Composable
private fun SettingsHeader() {
    Column {
        Text(
            text = "⚙️ Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Configure your protection",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accentColor: androidx.compose.ui.graphics.Color = Primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = accentColor,
                checkedTrackColor = accentColor.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
private fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted
        )
    }
}
