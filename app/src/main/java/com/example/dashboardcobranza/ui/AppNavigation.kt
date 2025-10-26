package com.example.dashboardcobranza.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dashboardcobranza.data.model.Usuario
import java.net.URLEncoder
import java.net.URLDecoder

/**
 * Define todas las rutas de navegación de la aplicación como constantes.
 * Esto evita errores de tipeo y centraliza la gestión de rutas.
 */
object AppRoutes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val INCIDENCES = "incidences"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    // Ruta con un argumento dinámico {kpiName}
    const val DETAIL_KPI_ROUTE = "detailKpi"
    const val DETAIL_KPI = "$DETAIL_KPI_ROUTE/{kpiName}"
    // Ruta con un argumento dinámico {incidenciaId}
    const val DETAIL_INCIDENCE_ROUTE = "detalleIncidencia"
    const val DETAIL_INCIDENCE = "$DETAIL_INCIDENCE_ROUTE/{incidenciaId}"
    // Ruta con un argumento dinámico {incidenciaId} para añadir/editar
    const val ADD_EDIT_INCIDENCE_ROUTE = "addEditIncidencia"
    const val ADD_EDIT_INCIDENCE = "$ADD_EDIT_INCIDENCE_ROUTE/{incidenciaId}"

    /**
     * Crea la ruta completa para navegar a la pantalla de detalle de un KPI,
     * codificando el nombre para que sea seguro para una URL.
     */
    fun createDetailKpiRoute(kpiName: String): String {
        val encodedKpiName = URLEncoder.encode(kpiName, "UTF-8")
        return "$DETAIL_KPI_ROUTE/$encodedKpiName"
    }
}

/**
 * Gestiona el grafo de navegación de la aplicación.
 * Define qué pantalla se muestra para cada ruta.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    // --- INICIO DE LA CORRECCIÓN ---
    // Añadimos el usuario como parámetro para poder pasarlo a las pantallas que lo necesiten.
    // Es 'nullable' porque cuando el usuario no ha iniciado sesión, este valor será nulo.
    usuario: Usuario?,
    // --- FIN DE LA CORRECCIÓN ---
    // Callbacks para que el NavHost pueda comunicarse con el exterior (MainActivity)
    onLoginSuccess: (Usuario) -> Unit,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppRoutes.LOGIN) {
            LoginRegisterScreen(
                // Cuando el login o registro es exitoso, llamamos al callback
                // que nos pasaron desde MainActivity.
                onLoginSuccess = onLoginSuccess,
                onRegisterSuccess = onLoginSuccess
            )
        }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                onKpiClick = { kpiName ->
                    // Navegamos a la pantalla de detalle usando la ruta segura
                    navController.navigate(AppRoutes.createDetailKpiRoute(kpiName))
                }
            )
        }

        composable(
            route = AppRoutes.DETAIL_KPI,
            arguments = listOf(navArgument("kpiName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Decodificamos el nombre del KPI para restaurar el texto original
            val kpiName = backStackEntry.arguments?.getString("kpiName")?.let {
                URLDecoder.decode(it, "UTF-8")
            }
            DetailKpiScreen(
                kpiName = kpiName,
                // Simplemente volvemos atrás en la pila de navegación
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.INCIDENCES) {
            // Asumiendo que IncidenciasScreen necesita el navController para navegar a detalles
            IncidenciasScreen(navController = navController)
        }

        composable(AppRoutes.PROFILE) {
            // --- INICIO DE LA CORRECCIÓN ---
            // Verificamos que el usuario no sea nulo antes de intentar mostrar la pantalla de perfil.
            // Si por alguna razón es nulo, no se mostrará nada para evitar un crash.
            // La lógica en MainActivity ya debería prevenir llegar aquí sin un usuario.
            usuario?.let { user ->
                PerfilScreen(
                    usuario = user, // Pasamos el objeto de usuario a la pantalla.
                    onLogout = onLogout
                )
            }
            // --- FIN DE LA CORRECCIÓN ---
        }

        composable(AppRoutes.SETTINGS) {
            SettingsScreen()
        }

        // Aquí irían las otras rutas como detalle y edición de incidencias...
        composable(
            route = AppRoutes.DETAIL_INCIDENCE,
            arguments = listOf(navArgument("incidenciaId") { type = NavType.IntType })
        ) {
            DetalleIncidenciaScreen(navController = navController)
        }

        composable(
            route = AppRoutes.ADD_EDIT_INCIDENCE,
            arguments = listOf(navArgument("incidenciaId") { type = NavType.IntType })
        ) {
            AddEditIncidenciaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
