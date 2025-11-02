package com.example.dashboardcobranza.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.ui.viewmodel.IncidenciasViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import java.io.File

/**
 * Pantalla que muestra la lista de incidencias.
 * Gestiona la visibilidad de las acciones (añadir, exportar) según el rol del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidenciasScreen(
    navController: NavController,
    viewModel: IncidenciasViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sessionState by sessionViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val esAdmin = sessionState.usuario?.esAdmin == true

    // Efecto para compartir el PDF cuando se genera en el ViewModel.
    LaunchedEffect(uiState.pdfGenerado) {
        uiState.pdfGenerado?.let {
            val file = File(context.cacheDir, "reporte_incidencias.pdf")
            file.writeBytes(it)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/pdf"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartir Reporte"))
            viewModel.onPdfCompartido()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (esAdmin) {
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FloatingActionButton(onClick = { navController.navigate("addEditIncidencia/-1") }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Incidencia")
                    }
                    FloatingActionButton(
                        onClick = { viewModel.generarReportePDF() },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "Generar Reporte")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.incidencias.isEmpty() -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(uiState.incidencias) { incidencia ->
                            IncidenciaListItem(incidencia = incidencia, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable que se muestra cuando no hay incidencias en la lista.
 */
@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Text("¡Todo en orden!", style = MaterialTheme.typography.titleLarge)
        Text("No hay incidencias pendientes.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Elemento individual de la lista de incidencias.
 * Muestra un resumen de la incidencia y un icono dinámico según su estado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncidenciaListItem(incidencia: Incidencia, navController: NavController) {
    val (icon, iconColor) = when (incidencia.estado) {
        "Nueva" -> Icons.Default.FiberNew to MaterialTheme.colorScheme.primary
        "En Progreso" -> Icons.Default.Sync to MaterialTheme.colorScheme.secondary
        "Cerrada" -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.tertiary
        "Bloqueada" -> Icons.Default.Block to MaterialTheme.colorScheme.error
        else -> Icons.Default.BugReport to MaterialTheme.colorScheme.onSurfaceVariant
    }

    ListItem(
        modifier = Modifier.clickable { navController.navigate("detalleIncidencia/${incidencia.id}") },
        headlineContent = { 
            Text(incidencia.titulo, fontWeight = FontWeight.Bold)
        },
        supportingContent = { 
            Column {
                Text("ID: #${incidencia.id} - Estado: ${incidencia.estado}")
                incidencia.assignedTo?.let {
                    Text("Asignado a: $it")
                }
            }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = "Estado: ${incidencia.estado}",
                modifier = Modifier.size(40.dp),
                tint = iconColor
            )
        },
        trailingContent = {
            PrioridadIndicator(prioridad = incidencia.prioridad)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        tonalElevation = 2.dp
    )
}
