package com.example.dashboardcobranza.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Representa el estado de la UI para la pantalla de Ajustes.
 */
data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val customKpiKeys: Set<String> = emptySet(),
    val appVersion: String = ""
)

/**
 * ViewModel para la pantalla de Ajustes.
 * Gestiona las preferencias del usuario (tema, notificaciones, KPIs)
 * y provee información de la app como la versión.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val app: Application
) : ViewModel() {

    /**
     * Canal para enviar eventos de una sola vez a la UI (ej. mostrar un Snackbar).
     * A diferencia de StateFlow, un Channel no guarda el estado, solo emite eventos.
     */
    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    // Obtenemos la versión de la app desde el PackageManager al iniciar el ViewModel.
    private val appVersionName: String = try {
        app.packageManager.getPackageInfo(app.packageName, 0).versionName
    } catch (e: Exception) {
        "?.?.?"
    }

    /**
     * El estado de la UI, construido de forma reactiva combinando los diferentes Flows
     * del [SettingsRepository] en un único objeto [SettingsUiState].
     */
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.isDarkMode,
        settingsRepository.notificationsEnabled,
        settingsRepository.customKpiKeys
    ) { isDarkMode, notificationsEnabled, customKpis ->
        SettingsUiState(
            isDarkMode = isDarkMode,
            notificationsEnabled = notificationsEnabled,
            customKpiKeys = customKpis,
            appVersion = appVersionName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(appVersion = appVersionName)
    )

    /**
     * Actualiza la preferencia de modo oscuro y envía un evento de confirmación a la UI.
     */
    fun onDarkModeChange(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(isDark)
            _uiEvents.send(if (isDark) "Modo oscuro activado" else "Modo claro activado")
        }
    }

    /**
     * Actualiza la preferencia de notificaciones y envía un evento de confirmación a la UI.
     */
    fun onNotificationsChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
            _uiEvents.send(if (enabled) "Notificaciones activadas" else "Notificaciones desactivadas")
        }
    }

    /**
     * Actualiza la lista de KPIs personalizados seleccionados por el usuario.
     */
    fun updateKpiSelection(kpiKey: String, isSelected: Boolean) {
        viewModelScope.launch {
            val currentSelection = uiState.value.customKpiKeys.toMutableSet()
            if (isSelected) {
                currentSelection.add(kpiKey)
            } else {
                currentSelection.remove(kpiKey)
            }
            settingsRepository.setCustomKpiKeys(currentSelection)
        }
    }
}
