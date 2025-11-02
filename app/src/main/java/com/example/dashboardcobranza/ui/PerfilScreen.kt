package com.example.dashboardcobranza.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dashboardcobranza.data.model.Usuario
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel

/**
 * Muestra la información del perfil del usuario que ha iniciado sesión.
 * Obtiene los datos del [SessionViewModel] compartido.
 */
@Composable
fun PerfilScreen(
    sessionViewModel: SessionViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val sessionState by sessionViewModel.uiState.collectAsState()
    val usuario = sessionState.usuario

    Box(modifier = Modifier.fillMaxSize()) {
        if (sessionState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (usuario != null) {
            ProfileContent(
                usuario = usuario,
                onLogout = onLogout
            )
        } else {
            Text("No se pudo cargar la información del perfil.", modifier = Modifier.align(Alignment.Center))
        }
    }
}

/**
 * El contenido visual de la pantalla de perfil.
 * @param usuario El objeto [Usuario] con los datos a mostrar.
 * @param onLogout Lambda que se ejecuta cuando el usuario presiona el botón de cerrar sesión.
 */
@Composable
private fun ProfileContent(
    usuario: Usuario,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Avatar y Nombre ---
        Box(
            modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, "Avatar", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(80.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(usuario.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(if (usuario.esAdmin) "Administrador" else usuario.puesto, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        // --- Detalles de Contacto e Información Laboral ---
        Card(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                 SectionTitle(title = "Detalles")
                 HorizontalDivider()
                 Spacer(Modifier.height(12.dp))
                 InfoRow(Icons.Default.AlternateEmail, usuario.email)
                 InfoRow(Icons.Default.Badge, "ID Usuario: ${usuario.id}")
                 InfoRow(Icons.Default.Badge, "Nº Empleado: ${usuario.numeroEmpleado}")
                 InfoRow(Icons.Default.Work, "Puesto: ${usuario.puesto}")
                 InfoRow(Icons.Default.Business, "Empresa: ${usuario.empresa}")
                 InfoRow(Icons.Default.LocationCity, "Ciudad: ${usuario.ciudad}")
            }
        }

        Spacer(Modifier.weight(1f))

        // --- Botón de Cerrar Sesión ---
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar Sesión", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Cerrar sesión")
        }
    }
}
