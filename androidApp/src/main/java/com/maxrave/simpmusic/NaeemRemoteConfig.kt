package com.maxrave.simpmusic

import android.content.Context
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Fetches the public, read-only Cloudflare configuration.
 * The last valid response is cached so the app remains usable offline.
 */
internal object NaeemRemoteConfig {
    private const val API_URL = "https://naeem-music-control-api.ansarinaeem5558.workers.dev/api/v1/config"
    private const val PREFS = "naeem_remote_config"
    private const val KEY_CONFIG = "last_good_config"

    fun fetch(context: Context, onResult: (JSONObject?) -> Unit) {
        Thread {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val result = try {
                val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                    setRequestProperty("Accept", "application/json")
                }
                try {
                    if (connection.responseCode !in 200..299) error("HTTP ${connection.responseCode}")
                    val body = connection.inputStream.bufferedReader().use { it.readText() }
                    val envelope = JSONObject(body)
                    val config = envelope.optJSONObject("config") ?: error("Missing config")
                    // Validate expected top-level groups before replacing the offline cache.
                    if (!config.has("branding") || !config.has("announcement") || !config.has("features")) {
                        error("Invalid configuration")
                    }
                    prefs.edit().putString(KEY_CONFIG, config.toString()).apply()
                    config
                } finally {
                    connection.disconnect()
                }
            } catch (_: Exception) {
                prefs.getString(KEY_CONFIG, null)?.let {
                    try { JSONObject(it) } catch (_: Exception) { null }
                }
            }
            onResult(result)
        }.start()
    }
}
