package com.example.dashboardcobranza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.dashboardcobranza.ui.AppNavigation
import com.example.dashboardcobranza.ui.AppRoutes
import com.example.dashboardcobranza.ui.MainAppBar
import com.example.dashboardcobranza.ui.theme.DashboardCobranzaTheme
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.SessionViewModel
import com.example.dashboardcobranza.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Define la estructura de un item de la barra de navegación inferior.
 */
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            DashboardCobranzaTheme(darkTheme = settingsState.isDarkMode) {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val sessionState by sessionViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(sessionState.isUserLoggedIn, sessionState.isLoading) {
        if (!sessionState.isLoading && !sessionState.isUserLoggedIn) {
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
    
    if (sessionState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (sessionState.isUserLoggedIn) AppRoutes.DASHBOARD else AppRoutes.LOGIN
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", AppRoutes.DASHBOARD, Icons.Default.Dashboard),
        BottomNavItem("Incidencias", AppRoutes.INCIDENCES, Icons.Default.Warning),
        BottomNavItem("Perfil", AppRoutes.PROFILE, Icons.Default.Person),
        BottomNavItem("Ajustes", AppRoutes.SETTINGS, Icons.Default.Settings)
    )

    val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }
    val onEditClick: (() -> Unit)? = if (currentDestination?.route == AppRoutes.PROFILE) {
        { navController.navigate(AppRoutes.EDIT_PROFILE) }
    } else {
        null
    }
    val onLogout = {
        sessionViewModel.logout()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBottomBar) {
                MainAppBar(
                    title = bottomNavItems.find { it.route == currentDestination?.route }?.label ?: "",
                    canNavigateBack = false,
                    onNavigateUp = {},
                    onEditClick = onEditClick
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination,
            sessionViewModel = sessionViewModel,
            dashboardViewModel = dashboardViewModel,
            onLoginSuccess = {
                sessionViewModel.onLoginSuccess(it)
            },
            onLogout = onLogout
        )
    }
}
