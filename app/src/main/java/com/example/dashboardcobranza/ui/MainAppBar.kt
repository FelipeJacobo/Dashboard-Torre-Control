package com.example.dashboardcobranza.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * Una TopAppBar personalizable para la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit,
    // El parámetro onLogout se elimina, ya que la acción ahora es exclusiva de la pantalla de perfil.
    onEditClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás"
                    )
                }
            }
        },
        actions = {
            // Si el callback de edición no es nulo, mostramos el botón.
            onEditClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Perfil"
                    )
                }
            }
            // SOLUCIÓN: Se elimina el IconButton de "Cerrar Sesión".
        }
    )
}
