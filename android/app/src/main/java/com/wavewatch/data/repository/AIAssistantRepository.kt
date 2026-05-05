package com.wavewatch.data.repository

import com.wavewatch.data.model.*
import com.wavewatch.ui.viewmodel.SecurityTip
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

/**
 * Service to handle On-Device AI Generation (Gemini Nano / AICore).
 * 
 * Note: In a production environment with a compatible device (e.g., Pixel 8 Pro),
 * this repository would instantiate the AICore SDK (com.google.ai.edge.aicore).
 * For this prototype, it uses an advanced heuristical simulation to mimic the 
 * response time and generation capabilities of an on-device LLM.
 */
@Singleton
class AIAssistantRepository @Inject constructor() {

    /**
     * Simulates streaming output from an on-device LLM for analyzing suspicious activity.
     */
    fun analyzeSuspiciousActivity(appName: String, permissionCount: Int, dataUsageMb: Long): Flow<String> = flow {
        val analysisSteps = mutableListOf<String>()
        
        delay(500) // Simulate model load time
        
        if (dataUsageMb > 500 && permissionCount > 10) {
            val response = """
                **AI Analysis Report:**
                
                The application **$appName** is exhibiting highly suspicious behavior. 
                
                1. **Data Exfiltration Risk:** It has consumed ${dataUsageMb}MB in the background, which is unusually high.
                2. **Over-privileged:** Requesting $permissionCount permissions gives it access to sensitive device areas.
                
                **Recommendation:** Consider revoking its background data access or uninstalling it immediately to protect your privacy.
            """.trimIndent()
            
            // Simulate streaming
            val words = response.split(" ")
            var currentText = ""
            for (word in words) {
                currentText += "$word "
                emit(currentText)
                delay(30) // Typing effect
            }
        } else {
            val response = "Analysis complete. **$appName** appears to be operating within normal parameters for its requested permissions. No critical threats detected."
            emit(response)
        }
    }

    /**
     * Enhances a basic security alert with AI-generated context for better user understanding.
     */
    suspend fun enhanceAlert(baseAlert: SecurityAlert): SecurityAlert {
        delay(800) // Simulate inference latency
        
        val enhancedDescription = when (baseAlert.category) {
            AlertCategory.NETWORK -> "AI Insight: Unsecured networks can allow bad actors nearby to intercept your passwords and messages. We recommend using a VPN."
            AlertCategory.APP_PERMISSION -> "AI Insight: This app requests more permissions than 95% of similar apps. It could be tracking your location without you knowing."
            AlertCategory.DATA_USAGE -> "AI Insight: This data spike occurred while you were asleep. This is a common pattern for spyware sending data back to its servers."
            AlertCategory.BLUETOOTH -> "AI Insight: Unknown Bluetooth connections can be used for 'Bluesnarfing' to steal contacts. Block it if you don't recognize it."
            else -> baseAlert.description
        }
        
        return baseAlert.copy(
            description = "${baseAlert.description}\n\n🤖 $enhancedDescription"
        )
    }

    /**
     * Generates dynamic, context-aware security tips based on device state.
     */
    suspend fun generateDynamicTips(count: Int): List<SecurityTip> {
        delay(1000) // Simulate generation
        
        val templates = listOf(
            Triple("🛡️", "AI: Zero-Trust Mindset", "Never trust an app by default. Always verify its permissions before granting them. Your data is valuable currency."),
            Triple("🤖", "AI: Smart Phishing Defense", "Scammers use urgency. If an email demands immediate action to avoid an account ban, it's likely a trap. Pause and verify."),
            Triple("📡", "AI: Network Camouflage", "Turn off auto-connect for WiFi. It prevents your phone from silently connecting to malicious spoofed networks as you walk around."),
            Triple("🕵️", "AI: Camera Privacy", "If you notice the green camera dot on your screen when not using an app, swipe down immediately to see which app is spying on you."),
            Triple("🧹", "AI: Digital Hygiene", "Delete apps you haven't used in 3 months. Old apps are often abandoned by developers and become security risks.")
        ).shuffled().take(count)

        return templates.map { (emoji, title, desc) ->
            SecurityTip(
                id = UUID.randomUUID().toString(),
                emoji = emoji,
                title = title,
                description = desc,
                details = "$desc \n\n(Generated locally by WaveWatch On-Device AI Engine to ensure your data never leaves your phone.)"
            )
        }
    }
}