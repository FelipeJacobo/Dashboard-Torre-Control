package com.example.dashboardcobranza.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dashboardcobranza.ui.viewmodel.DashboardViewModel
import com.example.dashboardcobranza.ui.viewmodel.SettingsViewModel

/**
 * Pantalla que permite al usuario seleccionar qué KPIs son visibles en el Dashboard.
 * Interactúa con dos ViewModels:
 * - [DashboardViewModel] para obtener la lista de todos los KPIs posibles.
 * - [SettingsViewModel] para guardar las preferencias de selección del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeDashboardScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel
) {
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    val selectedKpis = settingsUiState.customKpiKeys

    // La lista de todos los KPIs se obtiene del DashboardViewModel como única fuente de verdad.
    val allKpis = dashboardViewModel.allPossibleKpis

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personalizar Dashboard") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(allKpis) { kpi ->
                KpiSelectionItem(
                    kpiName = kpi.name,
                    isSelected = kpi.name in selectedKpis,
                    onCheckedChange = { isSelected ->
                        settingsViewModel.updateKpiSelection(kpi.name, isSelected)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

/**
 * Un elemento de la lista que representa un KPI seleccionable con un Checkbox.
 */
@Composable
private fun KpiSelectionItem(
    kpiName: String,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isSelected) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = kpiName, modifier = Modifier.weight(1f))
        Checkbox(
            checked = isSelected,
            onCheckedChange = onCheckedChange
        )
    }
}
