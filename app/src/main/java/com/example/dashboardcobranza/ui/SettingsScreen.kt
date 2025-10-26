package com.example.dashboardcobranza.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * La pantalla de ajustes, donde el usuario puede toquetear
 * las configuraciones de la app a su gusto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    // Variables para recordar si los interruptores están encendidos o apagados.
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    // Hacemos que toda la pantalla sea "scrolleable" por si nos emocionamos
    // y añadimos un montón de opciones más en el futuro.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Título de la sección de Apariencia
        Text(
            text = "Apariencia",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // La opción para pasarse al lado oscuro.
        SettingSwitchItem(
            title = "Modo Oscuro",
            subtitle = "Para los que prefieren no deslumbrarse.",
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Título de la sección de Notificaciones
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // El interruptor para decirle a la app "háblame" o "déjame en paz".
        SettingSwitchItem(
            title = "Recibir notificaciones",
            subtitle = "Alertas sobre incidencias y cambios importantes.",
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // La sección para los créditos y la info aburrida.
        Text(
            text = "Acerca de",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // La versión de la app, para saber si estamos a la última.
        SettingInfoItem(title = "Versión de la aplicación", value = "1.0.0-beta")
    }
}

/**
 * Un componente reutilizable para una opción con un interruptor.
 * Así nos ahorramos copiar y pegar todo el rato.
 */
@Composable
private fun SettingSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) } // Para que se pueda tocar toda la fila, no solo el switch.
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Pone el texto a un lado y el switch al otro.
    ) {
        Column(modifier = Modifier.weight(1f)) { // El texto ocupa todo el espacio que puede.
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Un grisecito para que no distraiga.
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * Otro componente reutilizable, pero este solo muestra texto.
 * Para cosas como la versión, que no se pueden cambiar.
 */
@Composable
private fun SettingInfoItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
