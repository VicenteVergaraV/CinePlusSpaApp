package com.cineplusapp.cineplusspaapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "session_prefs")
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    // Flow para observar el access token
    val accessTokenFlow: Flow<String?> =
        context.dataStore.data.map { it[KEY_AUTH_TOKEN] }

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

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
            prefs.remove(KEY_REFRESH_TOKEN)
        }
    }
}