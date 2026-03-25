package com.wavewatch.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotRepository @Inject constructor() {

    suspend fun explainThreat(alertTitle: String, alertDescription: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://api.openai.com/v1/chat/completions")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", "Bearer YOUR_API_KEY_HERE")
                connection.doOutput = true

                val body = JSONObject().apply {
                    put("model", "gpt-3.5-turbo")
                    put("messages", JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("content", "Explain this phone security threat in simple words. Threat: $alertTitle. Details: $alertDescription. Keep it under 3 sentences.")
                        })
                    })
                }

                connection.outputStream.write(body.toString().toByteArray())

                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

            } catch (e: Exception) {
                "Sorry, I couldn't explain this threat right now. Please try again."
            }
        }
    }
}
