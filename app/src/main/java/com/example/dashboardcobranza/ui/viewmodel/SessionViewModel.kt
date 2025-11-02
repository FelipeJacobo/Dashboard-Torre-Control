package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.SessionManager
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa el estado de la sesión del usuario en toda la aplicación.
 * @param isLoading Indica si se está verificando la sesión inicial.
 * @param isUserLoggedIn `true` si hay un usuario logueado, `false` en caso contrario.
 * @param usuario El objeto [Usuario] del usuario logueado, o `null`.
 */
data class SessionUiState(
    val isLoading: Boolean = true,
    val isUserLoggedIn: Boolean = false,
    val usuario: Usuario? = null
)

/**
 * ViewModel que actúa como la única fuente de la verdad para el estado de la sesión del usuario.
 * Es responsable de gestionar el login, logout y de proveer los datos del usuario actual
 * de forma reactiva a cualquier pantalla que lo necesite.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    /**
     * El estado de la UI, expuesto como un [StateFlow] reactivo.
     * Se construye combinando el [Flow] del ID de usuario del [SessionManager] con el [Flow] de la
     * base de datos del [UsuarioRepository]. Cada vez que el ID de sesión cambia (login/logout),
     * se suscribe automáticamente al flujo de datos del nuevo usuario, asegurando que la UI
     * siempre refleje el estado más reciente de la base de datos.
     */
    val uiState: StateFlow<SessionUiState> = sessionManager.loggedInUserIdFlow
        .flatMapLatest { userId ->
            if (userId == null) {
                // Si no hay ID de usuario, se emite un estado de "no logueado".
                flowOf(SessionUiState(isLoading = false, isUserLoggedIn = false, usuario = null))
            } else {
                // Si hay ID, se observa al usuario en la base de datos.
                usuarioRepository.obtenerUsuarioPorId(userId).map { usuario ->
                    SessionUiState(isLoading = false, isUserLoggedIn = true, usuario = usuario)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SessionUiState() // El estado inicial siempre es "cargando".
        )

    /**
     * Se llama cuando el login es exitoso para guardar el ID del usuario en la sesión.
     */
    fun onLoginSuccess(usuario: Usuario) {
        viewModelScope.launch {
            sessionManager.saveSession(usuario.id)
        }
    }

    /**
     * Borra la sesión del usuario.
     */
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }
}
