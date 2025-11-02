package com.example.dashboardcobranza.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Se define el DataStore a nivel de archivo para que solo haya una instancia.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Repositorio que gestiona las preferencias del usuario guardadas en [DataStore].
 * Es la única fuente de la verdad para todas las configuraciones de la app.
 */
@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Objeto privado que contiene las claves para acceder a las preferencias.
     * Usar un objeto como este previene errores de tipeo en los nombres de las claves.
     */
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val CUSTOM_KPI_KEYS = stringSetPreferencesKey("custom_kpi_keys")
    }

    /**
     * Un [Flow] que emite el estado actual del modo oscuro (`true` o `false`).
     * La UI se suscribe a este Flow para reaccionar a los cambios de tema.
     */
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        }

    /**
     * Un [Flow] que emite el estado de la preferencia de notificaciones.
     */
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        }

    /**
     * Un [Flow] que emite el conjunto de claves de los KPIs que el usuario ha seleccionado.
     */
    val customKpiKeys: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.CUSTOM_KPI_KEYS] ?: emptySet()
        }

    /**
     * Guarda la preferencia de modo oscuro en DataStore.
     */
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.IS_DARK_MODE] = isDark
        }
    }

    /**
     * Guarda la preferencia de notificaciones en DataStore.
     */
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    /**
     * Guarda el conjunto de KPIs seleccionados por el usuario en DataStore.
     */
    suspend fun setCustomKpiKeys(kpiKeys: Set<String>) {
        context.dataStore.edit {
            it[PreferencesKeys.CUSTOM_KPI_KEYS] = kpiKeys
        }
    }
}
