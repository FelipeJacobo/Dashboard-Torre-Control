package com.example.dashboardcobranza.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Prepara una instancia de DataStore que será única para toda la aplicación.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

/**
 * Gestiona la persistencia de la sesión del usuario (login, logout) usando Jetpack DataStore.
 * Al ser un Singleton, aseguramos que solo hay una instancia manejando el archivo de sesión.
 */
@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Definimos las "llaves" para acceder a nuestros datos, como en un diccionario.
    private companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = intPreferencesKey("user_id")
    }

    // Un Flow que emite `true` si el usuario está logueado, `false` si no.
    // Nos avisará en tiempo real si el estado cambia.
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    // Un Flow que emite el ID del usuario logueado, o nulo si no hay nadie.
    val loggedInUserIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]
        }

    /**
     * Guarda el ID del usuario en DataStore y marca la sesión como activa.
     */
    suspend fun saveSession(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
        }
    }

    /**
     * Limpia los datos de la sesión de DataStore.
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear() // Borra todas las preferencias de este DataStore.
        }
    }
}
