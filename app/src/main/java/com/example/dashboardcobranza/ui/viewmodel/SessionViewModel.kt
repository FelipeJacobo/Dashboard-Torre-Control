package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.SessionManager
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa el estado de la sesión, ahora con un estado de carga inicial.
 */
data class SessionUiState(
    val isLoading: Boolean = true, // true al inicio mientras comprobamos si hay sesión.
    val isUserLoggedIn: Boolean = false,
    val usuario: Usuario? = null
)

/**
 * Gestiona el estado de la sesión del usuario.
 * Ahora se encarga de comprobar si existe una sesión guardada al iniciar la app.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val sessionManager: SessionManager // Hilt inyectará nuestro SessionManager aquí.
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // En cuanto el ViewModel se crea, comprobamos si hay una sesión activa.
        checkActiveSession()
    }

    private fun checkActiveSession() {
        viewModelScope.launch {
            // Leemos el ID de usuario guardado de nuestro SessionManager.
            val userId = sessionManager.loggedInUserIdFlow.firstOrNull()
            if (userId != null) {
                // ¡Hay un ID! Buscamos los datos completos de ese usuario.
                val usuario = usuarioRepository.obtenerUsuarioPorId(userId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isUserLoggedIn = true,
                        usuario = usuario
                    )
                }
            } else {
                // No hay ningún ID guardado, así que no hay sesión.
                _uiState.update { it.copy(isLoading = false, isUserLoggedIn = false) }
            }
        }
    }

    /**
     * Se llama cuando el login es exitoso.
     */
    fun onLoginSuccess(usuario: Usuario) {
        viewModelScope.launch {
            // Le pedimos al SessionManager que guarde el ID del usuario.
            sessionManager.saveSession(usuario.id)
            // Actualizamos el estado de la UI.
            _uiState.update { it.copy(usuario = usuario, isUserLoggedIn = true, isLoading = false) }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     * Limpia la sesión persistida y actualiza el estado de la UI.
     */
    fun logout() { // <-- CORRECCIÓN: Renombramos 'onLogout' a 'logout'
        viewModelScope.launch {
            // Le pedimos al SessionManager que borre los datos de la sesión.
            sessionManager.clearSession()
            // Reseteamos el estado de la UI a sus valores iniciales, indicando que ya no está cargando.
            _uiState.update { SessionUiState(isLoading = false, isUserLoggedIn = false, usuario = null) }
        }
    }
}
