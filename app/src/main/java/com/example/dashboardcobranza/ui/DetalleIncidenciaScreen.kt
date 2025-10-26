package com.example.dashboardcobranza.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dashboardcobranza.ui.viewmodel.DetalleIncidenciaViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * La pantalla que muestra todos los jugosos detalles de una sola incidencia.
 * Es como la ficha de un personaje, pero para problemas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleIncidenciaScreen(
    // ¡Pásame el mapa! El NavController para que esta pantalla sepa cómo moverse.
    navController: NavHostController,
    // El ViewModel que se encarga de buscar y gestionar ESTA incidencia en particular.
    detalleViewModel: DetalleIncidenciaViewModel = hiltViewModel(),
    // El ViewModel "guardia de seguridad" para saber quién está mirando la pantalla.
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    // Nos ponemos a escuchar los chismes del ViewModel: ¿ya cargó? ¿hay un error? ¿qué incidencia es?
    val uiState by detalleViewModel.uiState.collectAsState()
    val sessionState by sessionViewModel.uiState.collectAsState()
    val context = LocalContext.current // Para poder lanzar Toasts y esas cosas.

    // ¿El que mira es un jefe? Guardamos si es admin o no.
    val esAdmin = sessionState.usuario?.esAdmin == true
    // Un interruptor para mostrar o no el diálogo de "¿seguro que quieres borrar esto?".
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    // Este bloque se despierta si `eliminacionCompletada` cambia a `true`.
    LaunchedEffect(uiState.eliminacionCompletada) {
        if (uiState.eliminacionCompletada) {
            Toast.makeText(context, "¡Puf! Incidencia eliminada.", Toast.LENGTH_SHORT).show()
            // Ya no hay nada que ver aquí, ¡vámonos patrás!
            navController.popBackStack()
        }
    }

    // Otro espía, pero este solo se activa si aparece un mensaje de error.
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // El título de la barra será el título de la incidencia. O un texto genérico si aún no carga.
                title = { Text(uiState.incidencia?.titulo ?: "Detalle de Incidencia") },
                navigationIcon = {
                    // El clásico botón para volver.
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                // Si estamos cargando, ponemos una ruedita para hipnotizar al usuario.
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.incidencia != null) {
                // ¡Llegaron los datos! Pintamos todo el detalle.
                val incidencia = uiState.incidencia!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()) // Para que se pueda hacer scroll si no cabe.
                ) {
                    // --- Aquí va todo el contenido del detalle ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip(label = "Estado", value = incidencia.estado)
                        PrioridadIndicator(prioridad = incidencia.prioridad)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = incidencia.descripcion, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider() // Una línea para separar visualmente.
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(label = "ID de Incidencia", value = "#${incidencia.id}")
                        InfoItem(
                            label = "Fecha de Creación",
                            value = formatFechaDetalle(incidencia.fechaCreacion),
                            alignment = Alignment.End
                        )
                    }

                    // Este Spacer es un truco para empujar los botones de abajo hacia el final.
                    Spacer(Modifier.weight(1f))

                    // --- La botonera de acción ---
                    if (esAdmin) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    // ¡A editar! Le decimos al nav que nos lleve a la pantalla de edición
                                    // pasándole el ID de esta misma incidencia.
                                    navController.navigate("addEditIncidencia/${incidencia.id}")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Editar")
                            }

                            Button(
                                onClick = { mostrarDialogoConfirmacion = true }, // Activa el diálogo de confirmación.
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            } else {
                // Si después de cargar, la incidencia sigue sin existir... algo salió mal.
                Text("Ups, no encontramos la incidencia.", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // El pop-up de confirmación que está escondido hasta que se necesita.
    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmacion = false }, // Si tocan fuera, se cierra.
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Seguro, seguro? Esta acción es como romper un plato, no hay vuelta atrás.") },
            confirmButton = {
                Button(
                    onClick = {
                        detalleViewModel.eliminarIncidencia() // Le damos la orden al ViewModel.
                        mostrarDialogoConfirmacion = false // Cerramos el diálogo.
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sí, eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoConfirmacion = false }) {
                    Text("Mejor no")
                }
            }
        )
    }
}


// --- Componentes Pequeños y Reutilizables ---

/** Un circulito de color con el nombre de la prioridad. */
@Composable
fun PrioridadIndicator(prioridad: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = when (prioridad) {
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
            text = prioridad,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Una "píldora" de información, como el estado. */
@Composable
fun InfoChip(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/** Un bloque de texto con etiqueta y valor, como el ID o la fecha. */
@Composable
fun InfoItem(label: String, value: String, alignment: Alignment.Horizontal = Alignment.Start) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/** Una función chiquita para que la fecha se vea bonita. */
private fun formatFechaDetalle(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
