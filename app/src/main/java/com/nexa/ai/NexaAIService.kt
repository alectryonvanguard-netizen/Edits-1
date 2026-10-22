
package com.nexa.ai

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NexaAIService {

    suspend fun sendMessage(
        message: String,
        apiKey: String
    ): String = withContext(Dispatchers.IO) {

        if (apiKey.isBlank()) {
            return@withContext "AI setup is incomplete. Please configure the API key."
        }

        var connection: HttpURLConnection? = null

        try {
            val url = URL(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
            )

            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 20000
                readTimeout = 60000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("x-goog-api-key", apiKey)
            }

            val body = JSONObject()
                .put(
                    "contents",
                    JSONArray().put(
                        JSONObject().put(
                            "parts",
                            JSONArray().put(
                                JSONObject().put("text", message)
                            )
                        )
                    )
                )

            connection.outputStream.use {
                it.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            val status = connection.responseCode
            val stream = if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = stream?.bufferedReader()?.use { it.readText() }.orEmpty()

            if (status !in 200..299) {
                return@withContext "AI request failed (HTTP $status). Check your API setup."
            }

            val json = JSONObject(response)
            json.optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.let { parts ->
                    (0 until parts.length())
                        .mapNotNull { parts.optJSONObject(it)?.optString("text") }
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                }
                ?.takeIf { it.isNotBlank() }
                ?: "I couldn't find a reply in the AI response."

        } catch (e: Exception) {
            "Couldn't reach Nexa AI. Check your internet connection and try again."
        } finally {
            connection?.disconnect()
        }
    }
}
