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
import androidx.navigation.NavController

/**
 * Pantalla que muestra un desglose del rendimiento mensual.
 * Presenta una versión más grande de la gráfica principal y un análisis de los datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyPerformanceScreen(
    navController: NavController
) {
    // Datos de ejemplo para la gráfica. En una app real, vendrían de un ViewModel.
    val dashboardChartData = mapOf(
        "Sem 1" to 85f, "Sem 2" to 92f, "Sem 3" to 78f, "Sem 4" to 88f
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Rendimiento") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Título de la Pantalla ---
            Text("Evolución Mensual del Rendimiento", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Análisis detallado de las últimas 4 semanas.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(16.dp))

            // --- Gráfica Principal ---
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SimpleColumnChart(
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        data = dashboardChartData
                    )
                }
            }
            
            // --- Datos Clave Resumidos ---
            val mejorSemana = dashboardChartData.maxByOrNull { it.value }
            val peorSemana = dashboardChartData.minByOrNull { it.value }
            val promedio = dashboardChartData.values.average()

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                if (mejorSemana != null) {
                    InfoChip(label = "Mejor Semana", value = "${mejorSemana.key} (${mejorSemana.value}%)")
                }
                InfoChip(label = "Promedio", value = "${String.format("%.1f", promedio)}%")
            }

            // --- Tarjeta de Análisis o Conclusiones ---
            if (peorSemana != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Área de Mejora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Se identificó el rendimiento más bajo en la ${peorSemana.key} con un ${peorSemana.value}%. Es una oportunidad para analizar las causas y aplicar mejoras.")
                    }
                }
            }
        }
    }
}
