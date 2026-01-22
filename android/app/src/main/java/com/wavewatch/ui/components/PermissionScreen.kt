package com.wavewatch.ui.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wavewatch.ui.theme.*

/**
 * Full-screen permission request for Usage Stats access
 */
@Composable
fun UsageStatsPermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    var permissionChecked by remember { mutableStateOf(0) }
    
    // Check permission when returning from settings
    LaunchedEffect(permissionChecked) {
        // This will re-run when permissionChecked changes
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Primary
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = "Permission Required",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = "WaveWatch needs access to Usage Statistics to monitor app behavior and protect your device.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = TextMuted
        )
        
        Spacer(Modifier.height(32.dp))
        
        Button(
            onClick = {
                openUsageStatsSettings(context)
                permissionChecked++
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("GRANT PERMISSION", fontWeight = FontWeight.Bold)
        }
        
        Spacer(Modifier.height(16.dp))
        
        TextButton(onClick = onPermissionGranted) {
            Text("Skip for now", color = TextMuted)
        }
    }
}

/**
 * Open system settings for Usage Stats permission
 */
fun openUsageStatsSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to main settings
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}

/**
 * Check if usage stats permission is granted
 */
fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.checkOpNoThrow(
        android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}
