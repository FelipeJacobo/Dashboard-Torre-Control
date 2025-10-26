package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * La "ficha" de estado para la pantalla de Login/Registro.
 * Aquí guardamos todo lo que la UI necesita saber: si está cargando, si hubo un error,
 * si el login o registro fue exitoso, y quién es el usuario que entró.
 */
data class LoginUiState(
    val isLoading: Boolean = false, // Para mostrar una ruedita de carga
    val error: String? = null, // Para mostrar mensajes de error
    val loginSuccess: Boolean = false, // Banderita para avisar que el login fue bueno
    val registrationSuccess: Boolean = false, // Banderita para avisar que el registro fue bueno
    val usuarioLogueado: Usuario? = null // Aquí guardamos los datos del usuario que inició sesión
)

/**
 * El ViewModel que maneja toda la lógica de inicio de sesión y registro.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository // Hilt nos da el repositorio para hablar con la BD de usuarios
) : ViewModel() {

    // El Flow privado donde manejamos el estado
    private val _uiState = MutableStateFlow(LoginUiState())
    // Y la versión pública que la UI observará para actualizarse
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Intenta iniciar sesión con un email y contraseña.
     */
    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val usuario = usuarioRepository.buscarPorEmail(email)

            if (usuario == null) {
                _uiState.update { it.copy(isLoading = false, error = "Usuario no encontrado.") }
            } else if (usuario.contrasena != contrasena) {
                // El campo en tu modelo se llama 'contrasena', no 'password'. Mantenemos la consistencia.
                _uiState.update { it.copy(isLoading = false, error = "Contraseña incorrecta.") }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true,
                        usuarioLogueado = usuario
                    )
                }
            }
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    fun register(nombre: String, email: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // --- INICIO DE LA CORRECCIÓN CLAVE ---
            try {
                // 1. Primero, verificamos si el email ya existe para dar un error claro.
                if (usuarioRepository.buscarPorEmail(email) != null) {
                    _uiState.update { it.copy(isLoading = false, error = "Este email ya está en uso.") }
                    return@launch
                }

                // 2. Creamos el objeto del nuevo usuario
                val esAdmin = email.equals("admin@cobranza.com", ignoreCase = true)
                val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena, esAdmin = esAdmin)

                // 3. Intentamos registrarlo a través del repositorio
                val exito = usuarioRepository.registrarUsuario(nuevoUsuario)

                if (exito) {
                    // 4. ¡CRUCIAL! Si el registro fue exitoso, buscamos al usuario para obtenerlo con su ID
                    val usuarioRecienCreado = usuarioRepository.buscarPorEmail(email)

                    if (usuarioRecienCreado != null) {
                        // 5. Ahora sí, actualizamos el estado con la bandera Y el objeto del usuario
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                registrationSuccess = true,
                                usuarioLogueado = usuarioRecienCreado // <-- ESTO ES LO QUE FALTABA
                            )
                        }
                    } else {
                        // Error muy improbable, pero es bueno manejarlo
                        _uiState.update { it.copy(isLoading = false, error = "Error crítico: El usuario se creó pero no se pudo recuperar.") }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo completar el registro.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error inesperado durante el registro.") }
            }
            // --- FIN DE LA CORRECCIÓN CLAVE ---
        }
    }

    /**
     * Reinicia el estado de la UI a su valor inicial.
     */
    fun resetState() {
        _uiState.update { LoginUiState() }
    }
}
