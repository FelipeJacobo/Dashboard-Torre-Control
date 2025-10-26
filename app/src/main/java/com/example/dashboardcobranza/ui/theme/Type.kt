package com.example.dashboardcobranza.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Aquí definimos el conjunto de estilos de texto para nuestra app.
// Es como la "hoja de estilos" para todas las letras que aparecen en pantalla.
val Typography = Typography(
    // Este es el estilo para el texto normal, el del "cuerpo" de la app.
    // Por ejemplo, la descripción de una incidencia.
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // La fuente por defecto del sistema.
        fontWeight = FontWeight.Normal, // Grosor normal, ni negrita ni fina.
        fontSize = 16.sp,               // Un tamaño de letra legible.
        lineHeight = 24.sp,             // Espacio entre líneas para que no se amontone.
        letterSpacing = 0.5.sp          // Un poquito de espacio entre letras.
    )

)
