package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.ui.viewmodel.AddEditIncidenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditIncidenciaScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditIncidenciaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var titulo by remember(uiState.incidencia?.titulo) { mutableStateOf(uiState.incidencia?.titulo ?: "") }
    var descripcion by remember(uiState.incidencia?.descripcion) { mutableStateOf(uiState.incidencia?.descripcion ?: "") }
    var prioridad by remember(uiState.incidencia?.prioridad) { mutableStateOf(uiState.incidencia?.prioridad ?: "Media") }
    var estado by remember(uiState.incidencia?.estado) { mutableStateOf(uiState.incidencia?.estado ?: "Nueva") }
    var assignedTo by remember(uiState.incidencia?.assignedTo) { mutableStateOf(uiState.incidencia?.assignedTo) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.incidencia != null) "Editar Incidencia" else "Añadir Incidencia") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (titulo.isNotBlank()) {
                    viewModel.guardarIncidencia(titulo, descripcion, prioridad, estado, assignedTo)
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
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
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
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                val userNames = listOf("Sin asignar") + uiState.usuarios.map { it.nombre }
                DropdownSelector(
                    label = "Asignar a",
                    options = userNames,
                    selectedOption = assignedTo ?: "Sin asignar",
                    onOptionSelected = { selected ->
                        assignedTo = if (selected == "Sin asignar") null else selected
                    }
                )
                
                DropdownSelector(
                    label = "Prioridad",
                    options = listOf("Baja", "Media", "Alta", "Crítica"),
                    selectedOption = prioridad,
                    onOptionSelected = { prioridad = it }
                )

                // SOLUCIÓN: El campo de estado ahora es siempre visible para el admin.
                DropdownSelector(
                    label = "Estado",
                    options = listOf("Nueva", "En Progreso", "Cerrada", "Bloqueada"),
                    selectedOption = estado,
                    onOptionSelected = { estado = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
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
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
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
