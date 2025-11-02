package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.SessionManager
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la UI para la pantalla de edición de perfil.
 */
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val user: Usuario? = null,
    val isSaveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UsuarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = sessionManager.loggedInUserIdFlow.first()
            if (userId != null) {
                // SOLUCIÓN: Consumimos el primer valor del Flow para obtener el objeto Usuario.
                val currentUser = userRepository.obtenerUsuarioPorId(userId).first()
                _uiState.update {
                    it.copy(user = currentUser, isLoading = false)
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Usuario no encontrado") }
            }
        }
    }

    fun updateUser(nombre: String, puesto: String, ciudad: String) {
        viewModelScope.launch {
            val currentUser = _uiState.value.user
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    nombre = nombre,
                    puesto = puesto,
                    ciudad = ciudad,
                    updatedAt = System.currentTimeMillis()
                )
                userRepository.actualizarUsuario(updatedUser)
                _uiState.update { it.copy(isSaveSuccess = true) }
            } else {
                _uiState.update { it.copy(error = "No se pudo actualizar. Usuario no encontrado.") }
            }
        }
    }
}
