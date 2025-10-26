package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.IncidenciaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * La "ficha" de estado de nuestra pantalla de detalle.
 * Contiene toda la información que la UI necesita para saber qué mostrar:
 * si está cargando, si hay un error, o los datos de la incidencia.
 */
data class DetalleIncidenciaUiState(
    val isLoading: Boolean = true, // Para mostrar una ruedita de carga
    val incidencia: Incidencia? = null, // Aquí guardaremos la incidencia cuando la encontremos
    val error: String? = null, // Por si algo sale mal
    val eliminacionCompletada: Boolean = false // Una banderita para avisar que ya se borró
)

@HiltViewModel
class DetalleIncidenciaViewModel @Inject constructor(
    private val repository: IncidenciaRepository, // Hilt nos pasa el repositorio para hablar con la BD
    savedStateHandle: SavedStateHandle // Esto es para pillar los argumentos que vienen de la navegación
) : ViewModel() {

    // El Flow privado donde manejamos el estado
    private val _uiState = MutableStateFlow(DetalleIncidenciaUiState())
    // Y la versión pública que la UI observará para pintarse
    val uiState: StateFlow<DetalleIncidenciaUiState> = _uiState.asStateFlow()

    // Sacamos el ID de la incidencia que nos pasaron al navegar a esta pantalla
    private val incidenciaId: Int? = savedStateHandle["incidenciaId"]

    init {
        // En cuanto se crea el ViewModel, nos ponemos a buscar la incidencia
        cargarDetalleIncidencia()
    }

    /**
     * Va a la base de datos a buscar los detalles de la incidencia por su ID.
     */
    private fun cargarDetalleIncidencia() {
        // Primero, una comprobación rápida: si no hay ID, no podemos buscar nada.
        if (incidenciaId == null || incidenciaId == 0) {
            _uiState.update { it.copy(isLoading = false, error = "ID de incidencia inválido") }
            return // Salimos de la función
        }

        // Lanzamos una corrutina para no bloquear la app mientras buscamos en la BD
        viewModelScope.launch {
            // Ponemos el estado en modo "cargando"
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Le pedimos al repositorio que nos traiga la incidencia
                val incidencia = repository.getIncidenciaById(incidenciaId)
                // ¡La encontramos! Actualizamos el estado con los datos y quitamos el "cargando"
                _uiState.update { it.copy(isLoading = false, incidencia = incidencia) }

            } catch (e: Exception) {
                // ¡Ups! Algo falló. Avisamos al estado para que la UI muestre un error.
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar la incidencia") }
            }
        }
    }

    /**
     * Se encarga de eliminar la incidencia que se está mostrando.
     */
    fun eliminarIncidencia() {
        // Si por alguna razón no tenemos una incidencia cargada, mejor no hacer nada.
        val incidenciaAEliminar = _uiState.value.incidencia ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Mostramos que estamos "trabajando"
            try {
                // Le decimos al repositorio que borre esta incidencia
                repository.eliminar(incidenciaAEliminar)

                // ¡Listo! Levantamos la banderita para que la UI sepa que se borró
                // y que puede, por ejemplo, volver a la pantalla anterior.
                _uiState.update { it.copy(isLoading = false, eliminacionCompletada = true) }

            } catch (e: Exception) {
                // Si algo sale mal durante el borrado, lo notificamos.
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar la incidencia") }
            }
        }
    }
}
