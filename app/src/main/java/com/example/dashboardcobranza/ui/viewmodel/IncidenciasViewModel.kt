package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.IncidenciaRepository
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.services.PDFGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa el estado de la UI para las pantallas relacionadas con incidencias.
 */
data class IncidenciasUiState(
    val incidencias: List<Incidencia> = emptyList(),
    val pdfGenerado: ByteArray? = null,
    val isLoading: Boolean = true
)

/**
 * ViewModel para la pantalla que muestra la lista de incidencias.
 * Es responsable de obtener las incidencias y de gestionar la generación del reporte en PDF.
 */
@HiltViewModel
class IncidenciasViewModel @Inject constructor(
    private val incidenciaRepository: IncidenciaRepository,
    private val pdfGenerator: PDFGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncidenciasUiState())
    val uiState: StateFlow<IncidenciasUiState> = _uiState.asStateFlow()

    init {
        // Se suscribe al Flow del repositorio para obtener actualizaciones en tiempo real.
        incidenciaRepository.obtenerTodas()
            .onEach { incidencias ->
                _uiState.update { it.copy(incidencias = incidencias, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Genera un reporte en PDF con la lista actual de incidencias.
     */
    fun generarReportePDF() {
        viewModelScope.launch {
            if (uiState.value.incidencias.isNotEmpty()) {
                _uiState.update { it.copy(isLoading = true) }
                val pdfBytes = pdfGenerator.generateIncidentsReport(uiState.value.incidencias)
                _uiState.update { it.copy(isLoading = false, pdfGenerado = pdfBytes) }
            }
        }
    }

    /**
     * Limpia el estado del PDF después de que ha sido procesado por la UI.
     */
    fun onPdfCompartido() {
        _uiState.update { it.copy(pdfGenerado = null) }
    }
}
