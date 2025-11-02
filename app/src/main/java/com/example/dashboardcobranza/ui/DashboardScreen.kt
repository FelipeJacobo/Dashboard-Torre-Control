package com.example.dashboardcobranza.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.dashboardcobranza.data.model.Usuario
import com.example.dashboardcobranza.ui.theme.Green40
import com.example.dashboardcobranza.ui.theme.Red40
import com.example.dashboardcobranza.ui.theme.Yellow40
import com.example.dashboardcobranza.ui.viewmodel.DashboardUiState
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.Kpi
import com.example.dashboardcobranza.ui.viewmodel.KpiStatus
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import java.io.File

/**
 * La pantalla principal del Dashboard.
 * Es responsable de observar el estado de los ViewModels y renderizar la UI correspondiente
 * (carga, estado vacío o contenido principal).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel,
    sessionViewModel: SessionViewModel,
    onKpiClick: (String) -> Unit,
    onChartClick: () -> Unit
) {
    val dashboardUiState by dashboardViewModel.uiState.collectAsState()
    val sessionUiState by sessionViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val esAdmin = sessionUiState.usuario?.esAdmin == true

    // Efecto secundario para lanzar el intent de compartir cuando el PDF está listo.
    LaunchedEffect(dashboardUiState.pdfGenerado) {
        dashboardUiState.pdfGenerado?.let {
            val file = File(context.cacheDir, "reporte_dashboard.pdf")
            file.writeBytes(it)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/pdf"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartir Reporte del Dashboard"))
            dashboardViewModel.onPdfCompartido()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (esAdmin) {
                FloatingActionButton(
                    onClick = { dashboardViewModel.generarReportePDF() },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    if (dashboardUiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Generar Reporte PDF")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                dashboardUiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                dashboardUiState.kpis.isEmpty() -> {
                    DashboardEmptyState(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    DashboardContent(
                        uiState = dashboardUiState,
                        usuario = sessionUiState.usuario,
                        onKpiClick = onKpiClick,
                        onChartClick = onChartClick
                    )
                }
            }
        }
    }
}

/**
 * Composable que se muestra cuando la lista de KPIs está vacía.
 */
@Composable
private fun DashboardEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.DashboardCustomize, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dashboard Vacío", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "No hay KPIs seleccionados. Ve a Ajustes para personalizar tu dashboard.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * El contenido principal del dashboard, mostrado en una grilla vertical.
 */
@Composable
private fun DashboardContent(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    usuario: Usuario?,
    onKpiClick: (String) -> Unit,
    onChartClick: () -> Unit
) {
    val dashboardChartData = mapOf(
        "Sem 1" to 85f, "Sem 2" to 92f, "Sem 3" to 78f, "Sem 4" to 88f
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Hola, ${usuario?.nombre ?: "Usuario"}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Este es el pulso de tu operación hoy.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onChartClick() },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rendimiento Mensual General",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    SimpleColumnChart(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        data = dashboardChartData
                    )
                }
            }
        }

        items(uiState.kpis) { kpi ->
            val icon = when(kpi.name) {
                "Eficiencia de Cobranza" -> Icons.AutoMirrored.Filled.TrendingUp
                "Tiempo de Resolución de Incidencias" -> Icons.Default.Speed
                "Clientes Gestionados por Agente" -> Icons.Default.Groups
                "Nivel de Morosidad" -> Icons.AutoMirrored.Filled.TrendingDown
                else -> Icons.Default.QueryStats
            }
            KpiCard(kpi = kpi, icon = icon, onClick = { onKpiClick(kpi.name) })
        }
    }
}

/**
 * Tarjeta individual para mostrar un KPI.
 */
@Composable
private fun KpiCard(kpi: Kpi, icon: ImageVector, onClick: () -> Unit) {
    val valueColor = when (kpi.status) {
        KpiStatus.GOOD -> Green40
        KpiStatus.WARNING -> Yellow40
        KpiStatus.BAD -> Red40
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).heightIn(min = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(imageVector = icon, contentDescription = kpi.name, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            
            Text(
                text = kpi.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = kpi.value,
                style = MaterialTheme.typography.displaySmall,
                color = valueColor,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = kpi.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
