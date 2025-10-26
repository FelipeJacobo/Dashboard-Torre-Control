package com.example.dashboardcobranza.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController // Importa el NavController
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.ui.viewmodel.IncidenciasViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla que muestra la lista de incidencias.
 * Ahora es responsable de su propia navegación.
 *
 * @param navController Controlador de navegación para moverse a otras pantallas.
 * @param sessionViewModel ViewModel para obtener el estado de la sesión (rol del usuario).
 * @param viewModel ViewModel para obtener la lista de incidencias.
 */
@Composable
fun IncidenciasScreen(
    // 1. La firma de la función ahora es mucho más simple.
    navController: NavHostController,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    viewModel: IncidenciasViewModel = hiltViewModel()
) {
    // 2. Observar los estados de los ViewModels.
    val sessionState by sessionViewModel.uiState.collectAsState()
    val incidenciasList by viewModel.incidencias.collectAsState(initial = emptyList())

    // 3. Determinar el rol del usuario de forma segura.
    val esAdmin = sessionState.usuario?.esAdmin == true

    Scaffold(
        floatingActionButton = {
            // La lógica de rol para el botón se mantiene.
            if (esAdmin) {
                FloatingActionButton(
                    onClick = {
                        // 4. Acción de navegación DIRECTA. Ya no se usa una lambda.
                        navController.navigate("addEditIncidencia/-1")
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Incidencia")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (incidenciasList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (esAdmin) {
                                "No hay incidencias. Presiona el botón + para agregar una."
                            } else {
                                "No hay incidencias registradas actualmente."
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                item {
                    Text(
                        "Registro de Incidencias",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(incidenciasList, key = { it.id }) { incidencia ->
                    IncidenciaCard(
                        incidencia = incidencia,
                        onClick = {
                            // 5. Acción de navegación DIRECTA al hacer clic en una tarjeta.
                            navController.navigate("detalleIncidencia/${incidencia.id}")
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidenciaCard(incidencia: Incidencia, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = incidencia.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = incidencia.estado.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = incidencia.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = when (incidencia.prioridad) {
                                    "Crítica" -> Color.Red
                                    "Alta" -> Color(0xFFFFA500) // Naranja
                                    "Media" -> Color.Yellow
                                    else -> Color.Green
                                },
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = incidencia.prioridad,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = formatFecha(incidencia.fechaCreacion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun formatFecha(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
