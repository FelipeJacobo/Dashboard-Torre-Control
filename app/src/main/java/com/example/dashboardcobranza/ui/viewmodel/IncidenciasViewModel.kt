package com.example.dashboardcobranza.ui.viewmodel

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
 * El ViewModel para la pantalla que muestra la lista de todas las incidencias.
 * Se encarga de obtener la lista y de manejar las acciones como agregar, guardar o eliminar.
 */
@HiltViewModel
class IncidenciasViewModel @Inject constructor(
    private val incidenciaRepository: IncidenciaRepository // Hilt nos da el repositorio para hablar con la BD
) : ViewModel() {

    /**
     * Un StateFlow que expone la lista de incidencias.
     * La UI se engancha a esto y se actualiza sola cada vez que la lista cambia en la BD.
     */
    val incidencias: StateFlow<List<Incidencia>> = incidenciaRepository.obtenerTodas()
        .stateIn( // Convertimos el Flow "frío" del repositorio en un Flow "caliente" (StateFlow)
            scope = viewModelScope, // Vive lo mismo que el ViewModel
            // Empieza a escuchar 5 segundos después de que la pantalla deja de estar visible. Ahorra batería.
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList() // Al principio, la lista está vacía mientras carga
        )

    /**
     * Crea una incidencia con datos al azar para hacer pruebas rápidas.
     * ¡Esto es solo para desarrollo, no para la app final!
     */
    fun agregarNuevaIncidencia() {
        // Lanzamos una corrutina para no congelar la pantalla.
        viewModelScope.launch {
            val titulos = listOf("Fallo en servidor", "Impresora no funciona", "Error de red", "Acceso a CRM denegado", "Lentitud en sistema")
            val prioridades = listOf("Baja", "Media", "Alta", "Crítica")

            val nueva = Incidencia(
                titulo = titulos.random(),
                prioridad = prioridades.random(),
                descripcion = "Detalle de la incidencia #${(100..999).random()}. Se requiere atención del equipo técnico.",
                estado = "Nueva"
            )
            // Le pasamos la nueva incidencia al repositorio para que la guarde.
            incidenciaRepository.insertar(nueva)
        }
    }

    /**
     * Guarda una incidencia. Sirve tanto para crear una nueva (si el ID es 0)
     * como para actualizar una que ya existe.
     */
    fun guardarIncidencia(
        id: Int,
        titulo: String,
        descripcion: String,
        prioridad: String,
        estado: String
    ) {
        viewModelScope.launch {
            // Buscamos si la incidencia ya existe para no perder la fecha de creación original.
            val incidenciaExistente = obtenerIncidenciaPorId(id)

            val incidenciaAGuardar = Incidencia(
                id = id,
                titulo = titulo,
                descripcion = descripcion,
                prioridad = prioridad,
                estado = estado,
                // Si ya existía, usamos su fecha; si no, ponemos la fecha y hora de ahora mismo.
                fechaCreacion = incidenciaExistente?.fechaCreacion ?: System.currentTimeMillis()
            )
            // El DAO se encargará de hacer un INSERT o un UPDATE gracias al "onConflict = REPLACE".
            incidenciaRepository.insertar(incidenciaAGuardar)
        }
    }

    /**
     * Elimina una incidencia de la base de datos usando su ID.
     */
    fun eliminarIncidencia(id: Int) {
        viewModelScope.launch {
            // El DAO necesita el objeto completo para borrarlo, no solo el ID.
            // Así que primero lo buscamos en nuestra lista.
            val incidenciaAEliminar = obtenerIncidenciaPorId(id)
            if (incidenciaAEliminar != null) {
                // Si lo encontramos, le pedimos al repositorio que lo elimine.
                incidenciaRepository.eliminar(incidenciaAEliminar)
            }
        }
    }

    /**
     * Una función de ayuda para buscar una incidencia en la lista actual por su ID.
     */
    fun obtenerIncidenciaPorId(id: Int?): Incidencia? {
        // Si el ID es nulo o 0, es una incidencia nueva o inválida, así que no buscamos nada.
        if (id == null || id == 0) return null
        // Usamos la función 'find' para buscar en la lista que ya tenemos en memoria.
        return incidencias.value.find { it.id == id }
    }
}
