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

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val registrationSuccess: Boolean = false,
    val usuarioLogueado: Usuario? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val usuario = usuarioRepository.buscarPorEmail(email.lowercase())

            if (usuario == null) {
                _uiState.update { it.copy(isLoading = false, error = "Usuario no encontrado.") }
            } else if (usuario.contrasena != contrasena) {
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

    fun register(
        nombre: String,
        email: String,
        contrasena: String,
        numeroEmpleado: String,
        puesto: String,
        empresa: String,
        ciudad: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // SOLUCIÓN: Se añade la validación de dominio.
                val emailLowerCase = email.lowercase()
                if (!emailLowerCase.endsWith("@coppel.com") && !emailLowerCase.endsWith("@cobranza.com")) {
                    _uiState.update { it.copy(isLoading = false, error = "El correo debe ser @coppel.com o @cobranza.com.") }
                    return@launch
                }

                if (usuarioRepository.buscarPorEmail(emailLowerCase) != null) {
                    _uiState.update { it.copy(isLoading = false, error = "Este correo electrónico ya está en uso.") }
                    return@launch
                }

                val esAdmin = emailLowerCase == "admin@cobranza.com"

                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    email = emailLowerCase,
                    contrasena = contrasena,
                    esAdmin = esAdmin,
                    numeroEmpleado = numeroEmpleado,
                    puesto = puesto,
                    empresa = empresa,
                    ciudad = ciudad
                )

                val nuevoId = usuarioRepository.insertar(nuevoUsuario)

                if (nuevoId != -1L) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationSuccess = true,
                            usuarioLogueado = nuevoUsuario.copy(id = nuevoId.toInt())
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo completar el registro.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error inesperado: ${e.message}") }
            }
        }
    }

    fun resetState() {
        _uiState.update { LoginUiState() }
    }
}
