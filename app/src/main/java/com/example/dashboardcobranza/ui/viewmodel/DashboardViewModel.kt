package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Enum para la semaforización. Es más seguro y legible que usar números o strings.
enum class KpiStatus {
    GOOD,    // Rendimiento bueno (verde)
    WARNING, // Rendimiento regular, requiere atención (amarillo)
    BAD      // Rendimiento malo, requiere acción inmediata (rojo)
}

// Modelo de datos actualizado para incluir el estado y descripciones claras.
data class Kpi(
    val name: String,         // Nombre del KPI (ej. "Eficiencia de Cobranza")
    val value: String,        // Valor principal que se muestra (ej. "95%")
    val description: String,  // Texto explicativo corto para la tarjeta
    val status: KpiStatus     // El estado para la semaforización
)

// Un data class para representar el estado completo de la pantalla del Dashboard.
data class DashboardUiState(
    val kpis: List<Kpi> = emptyList(),
    val isLoading: Boolean = true, // Para mostrar una rueda de carga
    val userName: String = ""      // Para el saludo personalizado
)

/**
 * El ViewModel para la pantalla del Dashboard.
 * Su trabajo es preparar y gestionar los datos (los KPIs) que la pantalla necesita.
 *
 * Actualizado para usar Hilt y un modelo de estado (UiState) más robusto.
 */
@HiltViewModel // Habilitamos la inyección de dependencias con Hilt
class DashboardViewModel @Inject constructor(
    // En el futuro, aquí inyectarías tus repositorios:
    // private val cobranzaRepository: CobranzaRepository
) : ViewModel() {

    // Flow privado para manejar el estado internamente
    private val _uiState = MutableStateFlow(DashboardUiState())
    // Flow público de solo lectura que la UI observará
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Simulamos una carga de red/base de datos para que la UI pueda mostrar un spinner.
            delay(1500)

            // Cargamos los KPIs específicos que definiste en tus requerimientos.
            val simulatedKpis = listOf(
                Kpi("Eficiencia de Cobranza", "95%", "% Recuperado vs. Meta", KpiStatus.GOOD),
                Kpi("Tiempo de Resolución", "2.5 Días", "Promedio para cerrar incidencias", KpiStatus.WARNING),
                Kpi("Clientes Gestionados", "67", "Contactos realizados por agente", KpiStatus.GOOD),
                Kpi("Nivel de Morosidad", "12%", "Cartera vencida > 30 días", KpiStatus.BAD),
                Kpi("Cumplimiento de Atención", "98%", "SLA de primer contacto", KpiStatus.GOOD)
            )

            // Actualizamos el estado completo de la UI de una sola vez
            _uiState.value = DashboardUiState(
                kpis = simulatedKpis,
                isLoading = false,
                userName = "Felipe Jacobo" // En el futuro, lo obtendríamos del SessionViewModel
            )
        }
    }

    /**
     * Busca y devuelve un KPI de la lista actual basándose en su nombre.
     * Esta función es necesaria para la DetailKpiScreen.
     *
     * @param kpiName El nombre del KPI a buscar.
     * @return El objeto Kpi si se encuentra, o null si no existe.
     */
    fun obtenerKpiPorNombre(kpiName: String?): Kpi? {
        if (kpiName.isNullOrBlank()) {
            return null
        }
        // Busca en la lista de KPIs que está dentro del estado actual (uiState.value)
        return uiState.value.kpis.find { it.name.equals(kpiName, ignoreCase = true) }
    }
}
