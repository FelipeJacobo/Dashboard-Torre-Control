package com.example.dashboardcobranza.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Se define una única instancia de DataStore para la sesión a nivel de archivo.
private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

/**
 * Gestiona la persistencia de la sesión del usuario.
 * Su única responsabilidad es guardar, leer y borrar el ID del usuario logueado.
 * Utiliza [DataStore] para asegurar que la sesión persista entre reinicios de la app.
 */
@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.sessionDataStore

    companion object {
        // Clave única para almacenar el ID del usuario en DataStore.
        private val LOGGED_IN_USER_ID = intPreferencesKey("logged_in_user_id")
    }

    /**
     * Guarda el ID del usuario en DataStore para iniciar una nueva sesión.
     */
    suspend fun saveSession(userId: Int) {
        dataStore.edit {
            it[LOGGED_IN_USER_ID] = userId
        }
    }

    /**
     * Un [Flow] que emite el ID del usuario logueado.
     * Emite `null` si no hay ninguna sesión activa.
     * El [SessionViewModel] observa este Flow para reaccionar a los cambios de login/logout.
     */
    val loggedInUserIdFlow: Flow<Int?> = dataStore.data.map {
        it[LOGGED_IN_USER_ID]
    }

    /**
     * Limpia el ID del usuario de DataStore para cerrar la sesión.
     */
    suspend fun clearSession() {
        dataStore.edit {
            it.remove(LOGGED_IN_USER_ID)
        }
    }
}
