package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Authenticated(val usuario: Usuario, val isNewUser: Boolean = false) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (email.isBlank() || contrasena.isBlank()) {
                _authState.value = AuthState.Error("Email y contraseña no pueden estar vacíos.")
                return@launch
            }
            val usuario = usuarioRepository.buscarPorEmail(email)
            if (usuario == null || usuario.contrasena != contrasena) {
                _authState.value = AuthState.Error("Credenciales incorrectas.")
            } else {
                _authState.value = AuthState.Authenticated(usuario)
            }
        }
    }

    /**
     * SOLUCIÓN: Se actualiza la firma de la función para aceptar todos los nuevos campos.
     */
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
            _authState.value = AuthState.Loading
            if (nombre.isBlank() || email.isBlank() || contrasena.isBlank() || numeroEmpleado.isBlank() || puesto.isBlank() || empresa.isBlank() || ciudad.isBlank()) {
                _authState.value = AuthState.Error("Todos los campos son requeridos.")
                return@launch
            }
            if (usuarioRepository.buscarPorEmail(email) != null) {
                _authState.value = AuthState.Error("El correo electrónico ya está registrado.")
                return@launch
            }

            val nuevoUsuario = Usuario(
                nombre = nombre,
                email = email,
                contrasena = contrasena,
                numeroEmpleado = numeroEmpleado,
                puesto = puesto,
                empresa = empresa,
                ciudad = ciudad
            )

            val id = usuarioRepository.insertar(nuevoUsuario)
            _authState.value = AuthState.Authenticated(nuevoUsuario.copy(id = id.toInt()), isNewUser = true)
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
