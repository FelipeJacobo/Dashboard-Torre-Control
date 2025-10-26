package com.example.dashboardcobranza.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.ui.theme.Green40
import com.example.dashboardcobranza.ui.theme.Red40
import com.example.dashboardcobranza.ui.theme.Yellow40
import com.example.dashboardcobranza.ui.viewmodel.DashboardUiState
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.Kpi
import com.example.dashboardcobranza.ui.viewmodel.KpiStatus

/**
 * Pantalla principal que muestra un resumen de los KPIs (Indicadores Clave de Rendimiento).
 */
@Composable
fun DashboardScreen(
    // Inyectamos el ViewModel usando Hilt para una correcta gestión del ciclo de vida y dependencias.
    viewModel: DashboardViewModel = hiltViewModel(),
    onKpiClick: (String) -> Unit // Callback para manejar clics y navegar
) {
    // Observamos el objeto de estado completo. La UI reaccionará a cualquier cambio en él.
    val uiState by viewModel.uiState.collectAsState()

    // Mostramos un indicador de carga mientras el ViewModel prepara los datos.
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Una vez cargados los datos, mostramos el contenido principal del dashboard.
        DashboardContent(
            uiState = uiState,
            onKpiClick = onKpiClick
        )
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onKpiClick: (String) -> Unit
) {
    // Datos de ejemplo para la gráfica del Dashboard.
    // En una app real, esto podría venir del mismo `uiState`.
    val dashboardChartData = mapOf(
        "Sem 1" to 85f, "Sem 2" to 92f, "Sem 3" to 78f, "Sem 4" to 88f
    )

    // Usamos una cuadrícula vertical perezosa para mostrar los KPIs.
    // Se adapta automáticamente y es eficiente con listas largas.
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Dos columnas fijas.
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre tarjetas horizontalmente.
        verticalArrangement = Arrangement.spacedBy(16.dp)   // Espacio entre tarjetas verticalmente.
    ) {
        // Creamos un "header" que ocupa todo el ancho (2 columnas) para el "Data Storytelling".
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Hola, ${uiState.userName}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Este es el pulso de tu operación hoy.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Añadimos un nuevo item que también ocupa el ancho completo para la gráfica.
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rendimiento Mensual General",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Usamos el Composable de gráfica reutilizable que creamos antes.
                    SimpleColumnChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        data = dashboardChartData
                    )
                }
            }
        }

        // Iteramos sobre la lista de KPIs del UiState y creamos una tarjeta para cada uno.
        items(uiState.kpis) { kpi ->
            KpiCard(kpi = kpi, onClick = { onKpiClick(kpi.name) })
        }
    }
}

/**
 * Tarjeta individual para mostrar la información de un KPI, con colores de estado.
 */
@Composable
private fun KpiCard(
    kpi: Kpi,
    onClick: () -> Unit
) {
    // Determinamos el color del texto principal basado en el estado del KPI (Semaforización).
    val valueColor = when (kpi.status) {
        KpiStatus.GOOD -> Green40
        KpiStatus.WARNING -> Yellow40
        KpiStatus.BAD -> Red40
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Hacemos la tarjeta interactiva.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Damos un color de fondo sutil a la tarjeta.
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .heightIn(min = 120.dp), // Altura mínima para consistencia, pero puede crecer si el texto es largo.
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Distribuye el espacio entre los elementos.
        ) {
            // Nombre del KPI (ej. "Eficiencia de Cobranza")
            Text(
                text = kpi.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            // Valor principal del KPI (ej. "95%")
            Text(
                text = kpi.value,
                style = MaterialTheme.typography.headlineMedium,
                color = valueColor, // Aplicamos el color de estado.
                fontWeight = FontWeight.ExtraBold
            )

            // Descripción corta
            Text(
                text = kpi.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
