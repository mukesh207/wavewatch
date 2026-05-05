package com.wavewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wavewatch.ui.theme.*
import com.wavewatch.ui.viewmodel.InsightsViewModel
import com.wavewatch.ui.viewmodel.NightWatchReport
import com.wavewatch.ui.viewmodel.DataUsageInfo
import com.wavewatch.ui.viewmodel.SecurityTip

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HeaderSection(onRefresh = viewModel::refresh) }
        
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading insights...", color = TextMuted)
                }
            }
        } else {
            uiState.nightWatchReport?.let { report ->
                item { NightWatchSection(report) }
            }
            
            uiState.dataUsage?.let { usage ->
                item { DataBudgetSection(usage) }
            }
            
            item { Text("💡 Learn", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            
            items(uiState.securityTips) { tip ->
                SecurityTipCard(tip)
            }
        }
    }
}

@Composable
private fun HeaderSection(onRefresh: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "📊 Insights",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your security intelligence",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }
        IconButton(onClick = onRefresh) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
        }
    }
}

@Composable
private fun NightWatchSection(report: NightWatchReport) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "🌙 Night Watch Report",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(Modifier.height(8.dp))
            Text(report.scanTime, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(value = report.appsScanned.toString(), label = "Apps Scanned")
                StatItem(value = report.threatsFound.toString(), label = "Threats", isWarning = report.threatsFound > 0)
                StatItem(value = report.dataLeaksBlocked.toString(), label = "Blocked")
            }
            
            Spacer(Modifier.height(12.dp))
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (report.threatsFound == 0) Success.copy(alpha = 0.15f) else Warning.copy(alpha = 0.15f)
            ) {
                Text(
                    text = report.status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = if (report.threatsFound == 0) Success else Warning,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, isWarning: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWarning && value != "0") Warning else MaterialTheme.colorScheme.onSurface
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
private fun DataBudgetSection(usage: DataUsageInfo) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📶 Data Budget",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Used this month", color = TextMuted)
                Text(
                    text = String.format("%.1f GB / %.0f GB", usage.usedGb, usage.totalGb),
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Custom progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(DarkSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(usage.usagePercentage.coerceIn(0f, 1f))
                        .height(8.dp)
                        .background(
                            when {
                                usage.usagePercentage > 0.9f -> Danger
                                usage.usagePercentage > 0.7f -> Warning
                                else -> Primary
                            }
                        )
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = String.format("%.1f GB remaining • Resets in %d days", usage.remainingGb, usage.daysUntilReset),
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun SecurityTipCard(tip: SecurityTip) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(tip.emoji, fontSize = 24.sp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tip.title,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = tip.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = isExpanded,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Divider(color = TextMuted.copy(alpha = 0.2f))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = tip.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                }
            }

            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (isExpanded) "Show Less" else "Read More",
                    fontSize = 12.sp,
                    color = Primary
                )
            }
        }
    }
}
