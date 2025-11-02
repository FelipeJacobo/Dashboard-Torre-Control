package com.example.dashboardcobranza.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dashboardcobranza.ui.viewmodel.DetalleIncidenciaViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel

/**
 * Pantalla que muestra la información detallada de una única incidencia.
 * Obtiene el ID de la incidencia desde la ruta de navegación y usa su propio
 * [DetalleIncidenciaViewModel] para cargar y gestionar los datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleIncidenciaScreen(
    navController: NavController,
    viewModel: DetalleIncidenciaViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sessionState by sessionViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    // Efecto para manejar la navegación hacia atrás una vez que la eliminación se completa.
    LaunchedEffect(uiState.eliminacionCompletada) {
        if (uiState.eliminacionCompletada) {
            Toast.makeText(context, "Incidencia eliminada.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.incidencia?.titulo ?: "Detalle de Incidencia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.incidencia != null) {
                val incidenciaActual = uiState.incidencia!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // --- Sección de Estado y Prioridad ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip("Estado", incidenciaActual.estado)
                        PrioridadIndicator(incidenciaActual.prioridad)
                    }

                    // --- Sección de Descripción ---
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(incidenciaActual.descripcion, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    // --- Sección de Detalles Adicionales ---
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InfoRow(icon = Icons.Default.Pin, text = "ID de Incidencia: #${incidenciaActual.id}")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            InfoRow(icon = Icons.Default.CalendarToday, text = "Creada el: ${formatFechaDetalle(incidenciaActual.fechaCreacion)}")
                            incidenciaActual.assignedTo?.let {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                InfoRow(icon = Icons.Default.Person, text = "Asignada a: $it")
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // --- Botones de Acción para Admin ---
                    if (sessionState.usuario?.esAdmin == true) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            OutlinedButton(
                                onClick = { navController.navigate("addEditIncidencia/${incidenciaActual.id}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Editar")
                            }

                            Button(
                                onClick = { mostrarDialogoConfirmacion = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            } else {
                Text("Ups, no encontramos la incidencia.", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmacion = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar esta incidencia?") },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.eliminarIncidencia()
                        mostrarDialogoConfirmacion = false 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sí, eliminar")
                }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogoConfirmacion = false }) { Text("Cancelar") } }
        )
    }
}
