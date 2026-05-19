package com.example.classroom20.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object GeminiManager {
    // Tvoj lični Groq API ključ
    private const val API_KEY = "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"
    private const val API_URL = "https://api.groq.com/openai/v1/chat/completions"

    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val url = URL(API_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $API_KEY")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15000
            connection.doOutput = true

            val jsonInput = JSONObject().apply {
                put("model", "llama-3.3-70b-versatile")
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", "Ti si pametan asistent. Odgovaraj isključivo na bosanskom/hrvatskom/srpskom jeziku.")
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
            }

            connection.outputStream.use { it.write(jsonInput.toString().toByteArray()) }

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(responseText)
                jsonObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() }
                when (responseCode) {
                    401 -> "Greška 401: API ključ nije validan. Molimo vas da kreirate svoj besplatan ključ na Groq konzoli."
                    429 -> "Previše zahtjeva. Sačekajte trenutak pa pokušajte ponovo."
                    else -> "Greška na serveru ($responseCode). Pokušajte ponovo kasnije."
                }
            }
        } catch (e: Exception) {
            "Problem sa konekcijom: ${e.localizedMessage}. Provjerite internet."
        }
    }
}
