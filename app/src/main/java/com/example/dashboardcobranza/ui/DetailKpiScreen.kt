package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dashboardcobranza.ui.theme.Green40
import com.example.dashboardcobranza.ui.theme.Red40
import com.example.dashboardcobranza.ui.theme.Yellow40
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.Kpi
import com.example.dashboardcobranza.ui.viewmodel.KpiStatus

/**
 * Muestra el detalle de un KPI específico, incluyendo su valor actual y una
 * gráfica de evolución histórica.
 * @param kpiName El nombre del KPI a mostrar, recibido desde la ruta de navegación.
 * @param viewModel El [DashboardViewModel] compartido, usado para obtener los datos del KPI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKpiScreen(
    kpiName: String?,
    onNavigateBack: () -> Unit,
    viewModel: DashboardViewModel
) {
    val kpi = viewModel.obtenerKpiPorNombre(kpiName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(kpi?.name ?: "Detalle de KPI") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { padding ->
        if (kpi == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("KPI no encontrado")
            }
            return@Scaffold
        }

        val chartData = mapOf(
            "T1" to 65f, "T2" to 70f, "T3" to 85f, "T4" to 92f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StatCard(kpi = kpi)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Gráfico de Evolución (Últimos 4 Trimestres)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SimpleColumnChart(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        data = chartData
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta principal que muestra el valor actual del KPI de forma prominente.
 */
@Composable
private fun StatCard(kpi: Kpi) {
    val valueColor = when (kpi.status) {
        KpiStatus.GOOD -> Green40
        KpiStatus.WARNING -> Yellow40
        KpiStatus.BAD -> Red40
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) 
        ) {
            Text("Valor Actual", style = MaterialTheme.typography.titleMedium)
            
            Text(
                text = kpi.value,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                color = valueColor
            )
            
            Text(kpi.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
