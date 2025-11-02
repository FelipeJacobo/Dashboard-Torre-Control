package com.example.dashboardcobranza.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dashboardcobranza.data.model.Usuario
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.IncidenciasViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import java.net.URLEncoder
import java.net.URLDecoder

/**
 * Objeto que centraliza todas las constantes de rutas de la aplicación
 * para una navegación segura y sin errores de tipeo.
 */
object AppRoutes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val INCIDENCES = "incidences"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MONTHLY_PERFORMANCE = "monthly_performance"
    const val EDIT_PROFILE = "edit_profile"
    const val CUSTOMIZE_DASHBOARD = "customize_dashboard"
    const val CHANGE_PASSWORD = "change_password"
    
    // Rutas con argumentos
    const val DETAIL_KPI_ROUTE = "detailKpi"
    const val DETAIL_KPI = "$DETAIL_KPI_ROUTE/{kpiName}"
    const val DETAIL_INCIDENCE_ROUTE = "detalleIncidencia"
    const val DETAIL_INCIDENCE = "$DETAIL_INCIDENCE_ROUTE/{incidenciaId}"
    const val ADD_EDIT_INCIDENCE_ROUTE = "addEditIncidencia"
    const val ADD_EDIT_INCIDENCE = "$ADD_EDIT_INCIDENCE_ROUTE/{incidenciaId}"

    fun createDetailKpiRoute(kpiName: String): String {
        val encodedKpiName = URLEncoder.encode(kpiName, "UTF-8")
        return "$DETAIL_KPI_ROUTE/$encodedKpiName"
    }
}

/**
 * Define el grafo de navegación de la aplicación.
 * Es el responsable de qué pantalla se muestra para cada ruta.
 * @param navController El controlador de navegación principal.
 * @param startDestination La ruta de inicio, calculada en `MainActivity`.
 * @param sessionViewModel La instancia compartida del ViewModel de sesión.
 * @param dashboardViewModel La instancia compartida del ViewModel del dashboard.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    sessionViewModel: SessionViewModel,
    dashboardViewModel: DashboardViewModel,
    onLoginSuccess: (Usuario) -> Unit,
    onLogout: () -> Unit
) {
    val incidenciasViewModel: IncidenciasViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppRoutes.LOGIN) {
            LoginRegisterScreen(onLoginSuccess = onLoginSuccess, onRegisterSuccess = onLoginSuccess)
        }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                dashboardViewModel = dashboardViewModel,
                sessionViewModel = sessionViewModel,
                onKpiClick = { kpiName -> navController.navigate(AppRoutes.createDetailKpiRoute(kpiName)) },
                onChartClick = { navController.navigate(AppRoutes.MONTHLY_PERFORMANCE) }
            )
        }

        composable(AppRoutes.MONTHLY_PERFORMANCE) {
            MonthlyPerformanceScreen(navController = navController)
        }

        composable(AppRoutes.CUSTOMIZE_DASHBOARD) {
            CustomizeDashboardScreen(navController = navController, dashboardViewModel = dashboardViewModel)
        }

        composable(AppRoutes.CHANGE_PASSWORD) {
            ChangePasswordScreen(navController = navController)
        }

        composable(
            route = AppRoutes.DETAIL_KPI,
            arguments = listOf(navArgument("kpiName") { type = NavType.StringType })
        ) { backStackEntry ->
            val kpiName = backStackEntry.arguments?.getString("kpiName")?.let { URLDecoder.decode(it, "UTF-8") }
            DetailKpiScreen(
                kpiName = kpiName,
                onNavigateBack = { navController.popBackStack() },
                viewModel = dashboardViewModel
            )
        }

        composable(AppRoutes.INCIDENCES) {
            IncidenciasScreen(navController = navController, viewModel = incidenciasViewModel)
        }

        composable(AppRoutes.PROFILE) {
            PerfilScreen(sessionViewModel = sessionViewModel, onLogout = onLogout)
        }

        composable(AppRoutes.SETTINGS) {
            SettingsScreen(navController = navController)
        }

        composable(AppRoutes.EDIT_PROFILE) {
            EditProfileScreen(navController = navController)
        }

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
            AddEditIncidenciaScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
