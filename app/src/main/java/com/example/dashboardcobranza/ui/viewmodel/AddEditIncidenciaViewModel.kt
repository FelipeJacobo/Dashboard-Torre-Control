package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.IncidenciaRepository
import com.example.dashboardcobranza.data.model.Incidencia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel específico para la pantalla de Añadir/Editar.
 * Obtiene el ID de la incidencia directamente desde los argumentos de navegación.
 */
@HiltViewModel
class AddEditIncidenciaViewModel @Inject constructor(
    private val incidenciaRepository: IncidenciaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Obtenemos el ID de la incidencia desde los argumentos de la URL ("addEditIncidencia/{incidenciaId}")
    val incidenciaId: Int? = savedStateHandle.get<Int>("incidenciaId")?.let {
        if (it == -1) null else it // Si el ID es -1 (crear nuevo), lo tratamos como nulo.
    }

    // Flujo de datos para obtener todas las incidencias, necesario para encontrar la que estamos editando.
    val incidencias: StateFlow<List<Incidencia>> = incidenciaRepository.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun guardarIncidencia(id: Int?, titulo: String, descripcion: String, prioridad: String, estado: String) {
        viewModelScope.launch {
            val incidenciaExistente = id?.let { findIncidenciaById(it) }

            val incidenciaAGuardar = Incidencia(
                id = id ?: 0, // Si el ID es nulo, Room generará uno nuevo (0 significa autogenerar).
                titulo = titulo,
                descripcion = descripcion,
                prioridad = prioridad,
                estado = estado,
                // Si la incidencia ya existía, mantenemos su fecha de creación original.
                fechaCreacion = incidenciaExistente?.fechaCreacion ?: System.currentTimeMillis()
            )
            incidenciaRepository.insertar(incidenciaAGuardar)
        }
    }

    fun eliminarIncidencia() {
        viewModelScope.launch {
            if (incidenciaId != null) {
                val incidenciaAEliminar = findIncidenciaById(incidenciaId)
                if (incidenciaAEliminar != null) {
                    incidenciaRepository.eliminar(incidenciaAEliminar)
                }
            }
        }
    }

    private fun findIncidenciaById(id: Int): Incidencia? {
        return incidencias.value.find { it.id == id }
    }
}
