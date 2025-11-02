package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Pantalla que muestra un desglose detallado del rendimiento mensual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyPerformanceDetailScreen(navController: NavController) {
    // Por ahora, usamos los mismos datos de ejemplo que el dashboard.
    // En una aplicación real, esto vendría de un ViewModel para obtener datos más detallados.
    val chartData = mapOf(
        "Sem 1" to 85f, "Sem 2" to 92f, "Sem 3" to 78f, "Sem 4" to 88f
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rendimiento Mensual Detallado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Evolución del Rendimiento en las Últimas 4 Semanas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Usamos el mismo componente de gráfica, pero le damos más espacio
            SimpleColumnChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f), // La gráfica ocupará el 70% del alto disponible
                data = chartData
            )

            // Aquí se podrían añadir más detalles en el futuro, como promedios, etc.
        }
    }
}
