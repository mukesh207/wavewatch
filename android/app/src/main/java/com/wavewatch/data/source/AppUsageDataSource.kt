package com.wavewatch.data.source

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import com.wavewatch.data.model.AppUsageInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for app usage statistics using Android's UsageStatsManager
 * Requires android.permission.PACKAGE_USAGE_STATS permission
 */
@Singleton
class AppUsageDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    private val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as? NetworkStatsManager
    private val packageManager = context.packageManager

    /**
     * Check if usage stats permission is granted
     */
    fun hasUsageStatsPermission(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val stats = usageStatsManager?.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            calendar.timeInMillis,
            System.currentTimeMillis()
        )
        return stats?.isNotEmpty() == true
    }

    /**
     * Get app usage info for all installed apps
     */
    suspend fun getAppUsageStats(daysBack: Int = 7): List<AppUsageInfo> = withContext(Dispatchers.IO) {
        if (usageStatsManager == null) return@withContext emptyList()

        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -daysBack)
        val startTime = calendar.timeInMillis

        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        val appUsageMap = mutableMapOf<String, AppUsageInfo>()

        usageStats?.forEach { stat ->
            try {
                val appInfo = packageManager.getApplicationInfo(stat.packageName, 0)
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val permissionCount = getPermissionCount(stat.packageName)
                val dataUsage = getDataUsageForApp(stat.packageName, startTime, endTime)
                val trustScore = calculateTrustScore(permissionCount, dataUsage)

                appUsageMap[stat.packageName] = AppUsageInfo(
                    packageName = stat.packageName,
                    appName = appName,
                    dataUsageBytes = dataUsage,
                    lastUsedTimestamp = stat.lastTimeUsed,
                    permissionCount = permissionCount,
                    trustScore = trustScore
                )
            } catch (e: PackageManager.NameNotFoundException) {
                // App might have been uninstalled
            }
        }

        appUsageMap.values
            .sortedByDescending { it.dataUsageBytes }
            .take(20)
    }

    /**
     * Get the number of permissions an app has
     */
    private fun getPermissionCount(packageName: String): Int {
        return try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_PERMISSIONS
            )
            packageInfo.requestedPermissions?.size ?: 0
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Get data usage for a specific app
     */
    private fun getDataUsageForApp(packageName: String, startTime: Long, endTime: Long): Long {
        if (networkStatsManager == null) return 0L

        return try {
            val uid = packageManager.getApplicationInfo(packageName, 0).uid
            var totalBytes = 0L

            // WiFi usage
            try {
                val wifiStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    null,
                    startTime,
                    endTime,
                    uid
                )
                val bucket = NetworkStats.Bucket()
                while (wifiStats.hasNextBucket()) {
                    wifiStats.getNextBucket(bucket)
                    totalBytes += bucket.rxBytes + bucket.txBytes
                }
                wifiStats.close()
            } catch (e: Exception) {
                // WiFi stats not available
            }

            // Mobile usage
            try {
                val mobileStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(),
                    startTime,
                    endTime,
                    uid
                )
                val bucket = NetworkStats.Bucket()
                while (mobileStats.hasNextBucket()) {
                    mobileStats.getNextBucket(bucket)
                    totalBytes += bucket.rxBytes + bucket.txBytes
                }
                mobileStats.close()
            } catch (e: Exception) {
                // Mobile stats not available
            }

            totalBytes
        } catch (e: Exception) {
            0L
        }
    }

    @Suppress("DEPRECATION")
    private fun getSubscriberId(): String? {
        return try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            telephonyManager?.subscriberId
        } catch (e: SecurityException) {
            null
        }
    }

    /**
     * Calculate trust score based on permissions and data usage
     */
    private fun calculateTrustScore(permissionCount: Int, dataUsage: Long): Int {
        var score = 100

        // Deduct for excessive permissions
        when {
            permissionCount > 20 -> score -= 40
            permissionCount > 15 -> score -= 25
            permissionCount > 10 -> score -= 15
            permissionCount > 5 -> score -= 5
        }

        // Deduct for high data usage (over 500MB)
        val dataUsageMB = dataUsage / (1024 * 1024)
        when {
            dataUsageMB > 2000 -> score -= 20
            dataUsageMB > 1000 -> score -= 15
            dataUsageMB > 500 -> score -= 10
        }

        return score.coerceIn(0, 100)
    }
}
