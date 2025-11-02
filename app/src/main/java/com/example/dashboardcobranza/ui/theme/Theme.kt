package com.example.dashboardcobranza.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Paleta de colores para el tema oscuro de la aplicación.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * Paleta de colores para el tema claro de la aplicación.
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * El Composable principal del tema de la aplicación.
 * Envuelve a toda la UI y le provee los colores, tipografías y formas de Material 3.
 * @param darkTheme Si es `true`, aplica la paleta de colores oscura. Por defecto, sigue la configuración del sistema.
 * @param dynamicColor Si es `true` (y el dispositivo es compatible con API 31+), usa colores dinámicos del wallpaper del sistema. Desactivado por defecto.
 * @param content El contenido de la UI al que se le aplicará el tema.
 */
@Composable
fun DashboardCobranzaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
