package com.cineplusapp.cineplusspaapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        private val KEY_USERS_JSON = stringPreferencesKey("registered_users_json")

        private val gson by lazy { Gson() }
    }

    // Flow para observar el access token
    val accessTokenFlow: Flow<String> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())   // <-- evita estar en null
                } else {
                    throw e
                }
            }
            .map { it[KEY_AUTH_TOKEN] ?: "" }
            .distinctUntilChanged()

    val isLoggedInFlow = accessTokenFlow.map { !it.isNullOrBlank() }

    // necesarios para el usuario al momento de registrarse
    private suspend fun getUsersMap(): MutableMap<String, Map<String, String>> {
        val json = context.dataStore.data.first()[KEY_USERS_JSON].orEmpty()
        if (json.isBlank()) return mutableMapOf()
        val type = object : TypeToken<MutableMap<String, Map<String, String>>>() {}.type
        return gson.fromJson(json, type) ?: mutableMapOf()
    }

    private suspend fun saveUsersMap(map: MutableMap<String, Map<String, String>>) {
        val json = gson.toJson(map)
        context.dataStore.edit { it[KEY_USERS_JSON] = json }
    }

    // encriptacion de valores
    private fun sha256(s: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(s.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    suspend fun registerUser(email: String, name: String, pass: String) {
        val map = getUsersMap()
        map[email.lowercase()] = mapOf("name" to name, "passHash" to sha256(pass))
        saveUsersMap(map)
    }

    suspend fun isUserRegistered(email: String): Boolean {
        val map = getUsersMap()
        return map.containsKey(email.lowercase())
    }

    suspend fun validateLocalCredentials(email: String, pass: String): Boolean {
        val map = getUsersMap()
        val u = map[email.lowercase()] ?: return false
        return u["passHash"] == sha256(pass)
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[KEY_AUTH_TOKEN] = token }
    }
    suspend fun getAuthToken(): String? =
        context.dataStore.data.map { it[KEY_AUTH_TOKEN] }.first()

    suspend fun saveRefreshToken(token: String?) {
        context.dataStore.edit { prefs ->
            if (token == null) prefs.remove(KEY_REFRESH_TOKEN)
            else prefs[KEY_REFRESH_TOKEN] = token
        }
    }
    suspend fun getRefreshToken(): String? =
        context.dataStore.data.map { it[KEY_REFRESH_TOKEN] }.first()

    suspend fun saveTokens(access: String, refresh: String?) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = access
            if (refresh == null) prefs.remove(KEY_REFRESH_TOKEN)
            else prefs[KEY_REFRESH_TOKEN] = refresh
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit {
            it[KEY_AUTH_TOKEN] = ""
            it[KEY_REFRESH_TOKEN] = ""
        }
    }
}