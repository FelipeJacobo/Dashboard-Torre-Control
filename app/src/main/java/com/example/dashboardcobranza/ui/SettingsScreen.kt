package com.example.dashboardcobranza.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dashboardcobranza.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding -> 
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) { SectionTitle(title = "Cuenta")
            SettingClickableItem(
                title = "Cambiar Contraseña",
                icon = Icons.Default.Password,
                onClick = { navController.navigate(AppRoutes.CHANGE_PASSWORD) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle(title = "Apariencia")
            SettingSwitchItem(
                title = "Modo Oscuro",
                subtitle = "Activa el tema oscuro en toda la aplicación.",
                checked = uiState.isDarkMode,
                onCheckedChange = { viewModel.onDarkModeChange(it) },
                icon = Icons.Default.Palette
            )
            SettingClickableItem(
                title = "Personalizar Dashboard",
                icon = Icons.Default.DashboardCustomize,
                onClick = { navController.navigate(AppRoutes.CUSTOMIZE_DASHBOARD) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle(title = "Notificaciones")
            SettingSwitchItem(
                title = "Recibir notificaciones",
                subtitle = "Alertas sobre incidencias y cambios importantes.",
                checked = uiState.notificationsEnabled,
                onCheckedChange = { viewModel.onNotificationsChange(it) },
                icon = Icons.Default.Notifications
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle(title = "Soporte")
            SettingClickableItem(
                title = "Reportar un problema",
                icon = Icons.Default.BugReport,
                onClick = { sendEmail(context, "soporte.dashboard@coppel.com", "Reporte de Problema - App Dashboard") }
            )
            SettingClickableItem(
                title = "Enviar sugerencia",
                icon = Icons.AutoMirrored.Filled.Send,
                onClick = { sendEmail(context, "sugerencias.dashboard@coppel.com", "Sugerencia para App Dashboard") }
            )
            SettingClickableItem(
                title = "Política de privacidad",
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                onClick = { openUrl(context, "https://www.coppel.com/aviso-de-privacidad") }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SectionTitle(title = "Acerca de")
            InfoRow(icon = Icons.Default.Info, text = "Versión de la aplicación: ${uiState.appVersion}")
        }
    }
}
