package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.Kpi
import com.example.dashboardcobranza.ui.viewmodel.KpiStatus
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKpiScreen(
    kpiName: String?,
    onNavigateBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val kpi = if (!uiState.isLoading) {
        viewModel.obtenerKpiPorNombre(kpiName)
    } else {
        null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(kpi?.name ?: "Detalle de KPI") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (kpi != null) {
                KpiDetailContent(kpi)
            } else {
                Text(
                    text = "No se pudo cargar la información para el KPI: $kpiName",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun KpiDetailContent(kpi: Kpi) {
    val valueColor = when (kpi.status) {
        KpiStatus.GOOD -> Green40
        KpiStatus.WARNING -> Yellow40
        KpiStatus.BAD -> Red40
    }

    val detailChartData = when (kpi.name) {
        "Cobranza Efectiva" -> mapOf("Ene" to 88f, "Feb" to 91f, "Mar" to 92.5f, "Abr" to 90f)
        "Clientes al Día" -> mapOf("Ene" to 80f, "Feb" to 82f, "Mar" to 85f, "Abr" to 84f)
        "Promedio de Atraso" -> mapOf("Ene" to 15f, "Feb" to 12f, "Mar" to 10f, "Abr" to 11f)
        "Resolución Primer Contacto" -> mapOf("Ene" to 75f, "Feb" to 78f, "Mar" to 82f, "Abr" to 80f)
        else -> mapOf("T1" to 70f, "T2" to 75f, "T3" to 80f, "T4" to 85f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = kpi.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = kpi.description,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

        Text(
            text = kpi.value,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
        Text(
            text = "Valor Actual",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))

        Text(
            text = "Gráfico de Evolución",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        SimpleColumnChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            data = detailChartData
        )
    }
}

@Composable
fun SimpleColumnChart(
    modifier: Modifier = Modifier,
    data: Map<String, Float>
) {
    val model = entryModelOf(*data.values.toTypedArray())
    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        data.keys.elementAt(value.toInt())
    }

    Chart(
        modifier = modifier,
        chart = columnChart(),
        model = model,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(
            valueFormatter = bottomAxisValueFormatter
        )
    )
}
