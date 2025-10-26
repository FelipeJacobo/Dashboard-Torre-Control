package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * La "ficha" con los datos del usuario que se mostrarán en la pantalla de perfil.
 * NOTA: Esta es una estructura local para la UI del perfil.
 * En una app más grande, probablemente reutilizarías el modelo de la capa de datos.
 */
data class Usuario(
    val nombre: String = "",
    val puesto: String = "",
    val email: String = "",
    val idEmpleado: String = "",
    val departamento: String = "",
    val miembroDesde: String = ""
)

/**
 * El ViewModel para la pantalla de Perfil.
 * Su trabajo es obtener y mostrar los datos del usuario actual y gestionar el cierre de sesión.
 */
class PerfilViewModel : ViewModel() {

    // Un "Flow" privado que podemos modificar. Aquí vive el objeto del usuario.
    private val _usuarioState = MutableStateFlow(Usuario())

    // La versión pública y de solo lectura que la UI observará para pintarse.
    val usuarioState: StateFlow<Usuario> = _usuarioState.asStateFlow()

    init {
        // En cuanto se crea el ViewModel, cargamos los datos del perfil.
        // En una app real, aquí llamarías a un repositorio para cargar
        // los datos del usuario que ha iniciado sesión de verdad.
        cargarDatosDeUsuario()
    }

    private fun cargarDatosDeUsuario() {
        // Simulamos que estamos cargando datos de un usuario de verdad.
        // Por ahora, son datos fijos para que la pantalla no se vea vacía.
        _usuarioState.update {
            it.copy(
                nombre = "Juan Pérez",
                puesto = "Ejecutivo CAT",
                email = "juan.perez@coppel.com",
                idEmpleado = "89754",
                departamento = "Cobranza Telefónica",
                miembroDesde = "15/03/2021"
            )
        }
    }

    /**
     * Gestiona el proceso de cierre de sesión.
     */
    fun cerrarSesion() {
        // Aquí iría la lógica real para cerrar la sesión:
        // 1. Borrar cualquier dato sensible del usuario que hayas guardado (en DataStore, SharedPreferences, etc.).
        // 2. Avisar a la UI que la sesión terminó, para que te mande de vuelta a la pantalla de Login.
        // Por ahora, solo imprimimos un mensaje para saber que se llamó a la función.
        println("Lógica de cierre de sesión ejecutada.")
    }
}
