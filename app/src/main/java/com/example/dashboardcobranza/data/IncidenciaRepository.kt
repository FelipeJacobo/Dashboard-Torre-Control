package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow

/**
 * Define el contrato para la capa de acceso a datos de las incidencias.
 * Abstrae el origen de los datos para los ViewModels.
 */
interface IncidenciaRepository {

    /**
     * Recupera un [Flow] de todas las incidencias para observación en tiempo real.
     */
    fun obtenerTodas(): Flow<List<Incidencia>>

    /**
     * Obtiene una incidencia por su ID como un [Flow] para observar cambios.
     */
    fun getIncidenciaById(id: Int): Flow<Incidencia?>

    /**
     * Inserta o actualiza una incidencia en la fuente de datos.
     */
    suspend fun insertar(incidencia: Incidencia)

    /**
     * Elimina una incidencia de la fuente de datos.
     */
    suspend fun eliminar(incidencia: Incidencia)
}
