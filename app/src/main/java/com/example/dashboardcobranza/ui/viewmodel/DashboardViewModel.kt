package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.repository.SettingsRepository
import com.example.dashboardcobranza.services.PDFGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * Define el estado de un Indicador Clave de Rendimiento (KPI).
 */
enum class KpiStatus { GOOD, WARNING, BAD }

/**
 * Modelo de datos para un KPI individual.
 */
data class Kpi(
    val name: String,
    val value: String,
    val description: String,
    val status: KpiStatus
)

/**
 * Representa el estado completo de la UI para la pantalla del Dashboard.
 */
data class DashboardUiState(
    val kpis: List<Kpi> = emptyList(),
    val isLoading: Boolean = true,
    val pdfGenerado: ByteArray? = null
)

/**
 * ViewModel para la pantalla del Dashboard.
 * Es responsable de cargar, actualizar y filtrar los KPIs, así como de
 * gestionar la generación de reportes en PDF.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val pdfGenerator: PDFGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    /**
     * Lista maestra con la definición de todos los KPIs posibles en la aplicación.
     * La UI usará esta lista para la pantalla de personalización.
     */
    val allPossibleKpis = listOf(
        Kpi("Eficiencia de Cobranza", "", "% Recuperado vs. Meta", KpiStatus.GOOD),
        Kpi("Tiempo de Resolución de Incidencias", "", "Promedio para cerrar incidencias", KpiStatus.WARNING),
        Kpi("Clientes Gestionados por Agente", "", "Contactos realizados por agente", KpiStatus.GOOD),
        Kpi("Nivel de Morosidad", "", "Cartera vencida > 30 días", KpiStatus.BAD),
        Kpi("Cumplimiento de Metas de Atención", "", "SLA de primer contacto", KpiStatus.GOOD),
        Kpi("Satisfacción del Cliente", "", "Resultados de encuestas", KpiStatus.GOOD)
    )

    init {
        // Un Flow que emite un pulso cada 5 segundos para forzar una actualización.
        val tickerFlow = flow { while (true) { emit(Unit); delay(5000) } }

        // Se combinan las preferencias del usuario con el ticker.
        // La UI se refrescará si el usuario cambia los KPIs O si pasan 5 segundos.
        settingsRepository.customKpiKeys
            .combine(tickerFlow) { keys, _ -> keys }
            .onEach { selectedKpiNames -> loadDashboardData(selectedKpiNames) }
            .launchIn(viewModelScope)
    }

    /**
     * Carga y simula los datos de los KPIs, filtrando según las preferencias del usuario.
     */
    private fun loadDashboardData(selectedKpiNames: Set<String>) {
        viewModelScope.launch {
            // La primera vez, se desactiva el loader principal después de la carga inicial.
            if (uiState.value.isLoading) {
                delay(1000) // Simula una carga de red
                _uiState.update { it.copy(isLoading = false) }
            }

            val kpisToShow = if (selectedKpiNames.isEmpty()) {
                allPossibleKpis
            } else {
                allPossibleKpis.filter { it.name in selectedKpiNames }
            }

            // Simula valores aleatorios para los KPIs que se van a mostrar.
            val simulatedKpis = kpisToShow.map { kpi ->
                when (kpi.name) {
                    "Eficiencia de Cobranza" -> kpi.copy(value = "${Random.nextInt(90, 99)}%")
                    "Tiempo de Resolución de Incidencias" -> kpi.copy(value = "${Random.nextInt(2, 5)}.${Random.nextInt(0, 9)} Días")
                    "Clientes Gestionados por Agente" -> kpi.copy(value = "${Random.nextInt(60, 80)}")
                    "Nivel de Morosidad" -> kpi.copy(value = "${Random.nextInt(8, 15)}%")
                    "Cumplimiento de Metas de Atención" -> kpi.copy(value = "${Random.nextInt(95, 100)}%")
                    "Satisfacción del Cliente" -> kpi.copy(value = "${Random.nextInt(85, 95)}%")
                    else -> kpi
                }
            }

            _uiState.update { it.copy(kpis = simulatedKpis) }
        }
    }

    /**
     * Llama al generador de PDF y actualiza el estado con el resultado.
     */
    fun generarReportePDF() {
        viewModelScope.launch {
            val pdfBytes = pdfGenerator.generateDashboardReport(uiState.value.kpis)
            _uiState.update { it.copy(pdfGenerado = pdfBytes) }
        }
    }

    /**
     * Limpia el estado del PDF después de que ha sido compartido por la UI.
     */
    fun onPdfCompartido() {
        _uiState.update { it.copy(pdfGenerado = null) }
    }

    /**
     * Busca un KPI en la lista de KPIs actualmente mostrados en la UI.
     */
    fun obtenerKpiPorNombre(kpiName: String?): Kpi? {
        if (kpiName.isNullOrBlank()) return null
        return uiState.value.kpis.find { it.name.equals(kpiName, ignoreCase = true) }
    }
}
