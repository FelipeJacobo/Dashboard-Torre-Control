package com.example.dashboardcobranza.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dashboardcobranza.data.model.Usuario

/**
 * La pantalla donde el usuario puede ver su propia información.
 * Es como su tarjeta de presentación dentro de la app.
 */
@Composable
fun PerfilScreen(
    // Pasamos el objeto del usuario (puede que aún no haya cargado, por eso es nulable)
    // y la función mágica para cuando quiera cerrar sesión.
    usuario: Usuario?,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Para que se pueda hacer scroll en pantallas pequeñas.
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Primero lo primero: ¿tenemos los datos del usuario?
        if (usuario != null) {
            // --- La sección del careto y el nombre ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Redondito y bonito.
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                // Un ícono de persona genérico, ¡pero queda bien!
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            // Mostramos el nombre del usuario, ¡que se sienta importante!
            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            // Un pequeño tag para saber si es un jefe o no.
            Text(
                text = if (usuario.esAdmin) "Administrador" else "Usuario",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            // --- Una tarjeta con más detalles del usuario ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Detalles del Usuario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Divider() // Una línea para separar.
                    Spacer(Modifier.height(12.dp))
                    // Pequeños renglones con su info.
                    InfoRow(icon = Icons.Default.AlternateEmail, text = usuario.email)
                    InfoRow(icon = Icons.Default.Badge, text = "ID Usuario: ${usuario.id}")
                }
            }

        } else {
            // Si por algún motivo cósmico el usuario es nulo, mostramos esto.
            Text("Mmm... no pudimos cargar tu información.")
        }

        Spacer(Modifier.height(24.dp))

        // --- El gran botón rojo para decir "adiós" ---
        Button(
            // Al hacer clic, simplemente llamamos a la función onLogout que nos pasaron.
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Cerrar sesión")
        }
    }
}

/**
 * Un renglón reutilizable para mostrar un ícono y un texto.
 * Así no repetimos código. ¡Bien por nosotros!
 */
@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Es decorativo, no necesita descripción.
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
