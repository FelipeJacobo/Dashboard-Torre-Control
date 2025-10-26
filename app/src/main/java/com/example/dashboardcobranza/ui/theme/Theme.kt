package com.example.dashboardcobranza.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores para cuando el móvil está en modo oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Paleta de colores para el modo claro de toda la vida.
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    /* Si quisieras afinar más, aquí podrías cambiar otros colores
       como el fondo, el color del texto, etc. */
)

/**
 * Este es el Composable que viste a toda nuestra app.
 * Lo pones arriba del todo y ¡listo!, todo lo que esté dentro
 * pilla los colores y las fuentes que definimos aquí.
 */
@Composable
fun DashboardCobranzaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Pilla automáticamente si el sistema está en modo oscuro.
    dynamicColor: Boolean = true, // Para que en Android 12+ la app coja los colores del fondo de pantalla. ¡Mola mucho!
    content: @Composable () -> Unit
) {
    // Aquí está la magia: decidimos qué paleta de colores usar.
    val colorScheme = when {
        // ¿El usuario tiene Android 12+ y quiere colores dinámicos? ¡Pues se los damos!
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Usamos la paleta que genera Android a partir del wallpaper.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Si no, ¿está en modo oscuro? Pues usamos nuestra paleta oscura.
        darkTheme -> DarkColorScheme
        // Y si no, pues la de modo claro de siempre.
        else -> LightColorScheme
    }

    // Este bloque se encarga de que la barra de estado (la de arriba) combine con el tema.
    // Ahora, vamos a arreglar la barra de estado para que no desentone con nuestros colores.

    val view = LocalView.current
    if (!view.isInEditMode) { // Esto es para que no se rompa la vista previa de Android Studio.
        SideEffect {
            val window = (view.context as Activity).window
            // Le ponemos el color principal de nuestro tema.
            window.statusBarColor = colorScheme.primary.toArgb()
            // Y le decimos a Android si los iconitos de la barra (hora, wifi) deben ser claros u oscuros
            // para que se puedan leer bien.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Y finalmente, aplicamos todo el tema a nuestra app.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Las fuentes que definimos en Type.kt
        content = content // Aquí va el contenido de nuestra app
    )
}
