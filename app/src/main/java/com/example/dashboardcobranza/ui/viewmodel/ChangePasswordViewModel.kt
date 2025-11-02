package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.SessionManager
import com.example.dashboardcobranza.data.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UsuarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = false) }

            // Validación de campos vacíos
            if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                _uiState.update { it.copy(isLoading = false, error = "Todos los campos son obligatorios.") }
                return@launch
            }

            // Validación de coincidencia de nueva contraseña
            if (newPassword != confirmPassword) {
                _uiState.update { it.copy(isLoading = false, error = "Las nuevas contraseñas no coinciden.") }
                return@launch
            }

            val userId = sessionManager.loggedInUserIdFlow.first()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, error = "Error de sesión. Intenta iniciar sesión de nuevo.") }
                return@launch
            }

            val user = userRepository.obtenerUsuarioPorId(userId).first()
            if (user == null) {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo encontrar el usuario.") }
                return@launch
            }

            // Validación de contraseña actual
            if (user.contrasena != currentPassword) {
                _uiState.update { it.copy(isLoading = false, error = "La contraseña actual es incorrecta.") }
                return@launch
            }

            // Si todo es correcto, actualizamos al usuario
            val updatedUser = user.copy(contrasena = newPassword)
            userRepository.actualizarUsuario(updatedUser)
            
            _uiState.update { it.copy(isLoading = false, success = true) }
        }
    }

    fun resetState() {
        _uiState.value = ChangePasswordUiState()
    }
}
