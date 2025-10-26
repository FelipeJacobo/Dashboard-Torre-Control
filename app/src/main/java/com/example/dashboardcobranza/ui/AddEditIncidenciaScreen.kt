package com.example.dashboardcobranza.ui

// import androidx.activity.result.launch // ERROR 1: Eliminamos este import no necesario que causa conflicto.
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.ui.viewmodel.AddEditIncidenciaViewModel // Se asume que moverás el ViewModel a su propia carpeta

/**
 * La pantalla de formulario para Añadir o Editar una incidencia.
 * Ahora es autónoma y obtiene el ID a través de su propio ViewModel.
 *
 * @param onNavigateBack Función lambda para navegar hacia atrás.
 * @param viewModel El ViewModel que maneja la lógica de negocio de esta pantalla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditIncidenciaScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditIncidenciaViewModel = hiltViewModel()
) {
    // El ID ahora vive en el ViewModel
    val incidenciaId = viewModel.incidenciaId
    // Recolectamos la lista de incidencias como estado para que el LaunchedEffect reaccione
    val incidencias by viewModel.incidencias.collectAsState()

    // ---- ESTADOS DEL FORMULARIO ----
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("Media") }
    var estado by remember { mutableStateOf("Nueva") }
    var datosCargados by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val isEditing = incidenciaId != null

    // Este LaunchedEffect se ejecuta cuando 'incidencias' cambia (es decir, cuando se carga la data)
    LaunchedEffect(key1 = incidencias) {
        if (isEditing && !datosCargados) {
            val incidenciaExistente = incidencias.find { it.id == incidenciaId }

            if (incidenciaExistente != null) {
                titulo = incidenciaExistente.titulo
                descripcion = incidenciaExistente.descripcion
                prioridad = incidenciaExistente.prioridad
                estado = incidenciaExistente.estado
                datosCargados = true
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar esta incidencia? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarIncidencia() // El ViewModel ya sabe el ID
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Incidencia" else "Añadir Incidencia") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Incidencia",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (titulo.isNotBlank()) {
                    viewModel.guardarIncidencia(
                        id = incidenciaId,
                        titulo = titulo,
                        descripcion = descripcion,
                        prioridad = prioridad,
                        estado = estado
                    )
                    onNavigateBack()
                }
            }) {
                Icon(Icons.Default.Done, contentDescription = "Guardar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = titulo.isBlank()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it }, // ERROR 2: Corregido onValueeChange -> onValueChange
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            val prioridades = listOf("Baja", "Media", "Alta", "Crítica")
            DropdownSelector(
                label = "Prioridad",
                options = prioridades,
                selectedOption = prioridad,
                onOptionSelected = { prioridad = it }
            )

            val estados = listOf("Nueva", "En Progreso", "Cerrada", "Bloqueada")
            DropdownSelector(
                label = "Estado",
                options = estados,
                selectedOption = estado,
                onOptionSelected = { estado = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
