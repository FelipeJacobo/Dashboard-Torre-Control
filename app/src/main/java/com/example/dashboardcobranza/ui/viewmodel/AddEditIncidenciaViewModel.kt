package com.example.dashboardcobranza.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboardcobranza.data.IncidenciaRepository
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.model.Usuario
import com.example.dashboardcobranza.data.repository.SettingsRepository
import com.example.dashboardcobranza.services.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val incidencia: Incidencia? = null,
    val usuarios: List<Usuario> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AddEditIncidenciaViewModel @Inject constructor(
    private val incidenciaRepository: IncidenciaRepository,
    private val usuarioRepository: UsuarioRepository,
    // SOLUCIÓN: Se inyectan los servicios necesarios.
    private val notificationService: NotificationService,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val incidenciaId: Int? = savedStateHandle.get<Int>("incidenciaId")?.let { if (it == -1) null else it }

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val usuariosFlow = usuarioRepository.obtenerTodosLosUsuarios()
            val incidenciaFlow = incidenciaId?.let { incidenciaRepository.getIncidenciaById(it) } 
                ?: kotlinx.coroutines.flow.flowOf(null)

            combine(usuariosFlow, incidenciaFlow) { usuarios, incidencia ->
                AddEditUiState(usuarios = usuarios, incidencia = incidencia, isLoading = false)
            }.first().let { state ->
                _uiState.value = state
            }
        }
    }

    fun guardarIncidencia(
        titulo: String, 
        descripcion: String, 
        prioridad: String, 
        estado: String, 
        assignedTo: String?
    ) {
        viewModelScope.launch {
            val originalAssignedTo = uiState.value.incidencia?.assignedTo
            val incidenciaAGuardar = uiState.value.incidencia?.copy(
                titulo = titulo,
                descripcion = descripcion,
                prioridad = prioridad,
                estado = estado,
                assignedTo = assignedTo
            ) ?: Incidencia(
                titulo = titulo,
                descripcion = descripcion,
                prioridad = prioridad,
                estado = estado,
                assignedTo = assignedTo
            )
            incidenciaRepository.insertar(incidenciaAGuardar)

            // --- Lógica de Notificación ---
            // Se notifica solo si el `assignedTo` ha cambiado y no es nulo.
            if (assignedTo != null && assignedTo != originalAssignedTo) {
                val notificationsEnabled = settingsRepository.notificationsEnabled.first()
                if (notificationsEnabled) {
                    notificationService.showNewIncidenciaNotification(incidenciaAGuardar)
                }
            }
        }
    }

    fun eliminarIncidencia() {
        viewModelScope.launch {
            uiState.value.incidencia?.let { incidenciaRepository.eliminar(it) }
        }
    }
}
