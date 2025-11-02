package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.IncidenciaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleIncidenciaUiState(
    val isLoading: Boolean = true,
    val incidencia: Incidencia? = null,
    val error: String? = null,
    val eliminacionCompletada: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetalleIncidenciaViewModel @Inject constructor(
    private val repository: IncidenciaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val incidenciaId: Int = savedStateHandle["incidenciaId"] ?: 0

    // SOLUCIÓN: El estado de la UI ahora es un Flow que reacciona a los cambios de la base de datos.
    val uiState: StateFlow<DetalleIncidenciaUiState> = repository.getIncidenciaById(incidenciaId)
        .map { incidencia ->
            // Cada vez que la incidencia cambie en la BD, se emitirá un nuevo estado.
            DetalleIncidenciaUiState(incidencia = incidencia, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DetalleIncidenciaUiState(isLoading = true) // El estado inicial es "cargando"
        )

    // El _uiState se mantiene solo para manejar eventos puntuales como la eliminación.
    private val _eventState = MutableStateFlow(DetalleIncidenciaUiState())

    fun eliminarIncidencia() {
        val incidenciaAEliminar = uiState.value.incidencia ?: return

        viewModelScope.launch {
            _eventState.update { it.copy(isLoading = true) } 
            try {
                repository.eliminar(incidenciaAEliminar)
                _eventState.update { it.copy(isLoading = false, eliminacionCompletada = true) }
            } catch (e: Exception) {
                _eventState.update { it.copy(isLoading = false, error = "Error al eliminar la incidencia") }
            }
        }
    }
}
