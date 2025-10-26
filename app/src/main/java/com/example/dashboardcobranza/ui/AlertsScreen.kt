package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * La pantalla que muestra las alertas importantes del sistema.
 * Por ahora, muestra una alerta de ejemplo de forma estática.
 */
@Composable
fun AlertsScreen() {
    // Usamos una Columna para apilar los elementos verticalmente.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible.
            .padding(16.dp) // Un margen general para que no se pegue a los bordes.
    ) {
        // El título de la pantalla.
        Text("Alertas", style = MaterialTheme.typography.headlineMedium)

        // Un pequeño espacio para separar el título del contenido.
        Spacer(Modifier.height(8.dp))

        // Usamos una Card para que la alerta destaque visualmente.
        Card(
            modifier = Modifier
                .fillMaxWidth() // La tarjeta ocupa todo el ancho.
                .padding(6.dp) // Un pequeño margen exterior para la tarjeta.
        ) {
            // Otra columna para organizar el texto dentro de la tarjeta.
            Column(modifier = Modifier.padding(10.dp)) { // Margen interior para el texto.
                // El título de la alerta.
                Text(
                    "Campaña A - Caída de rendimiento",
                    style = MaterialTheme.typography.titleMedium
                )
                // La descripción de la alerta.
                Text(
                    "Se detectó una caída en la tasa de cobranza.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        // Aquí podrías añadir más Cards o un LazyColumn si esperas muchas alertas.
    }
}
