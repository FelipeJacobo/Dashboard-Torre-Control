package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow

/**
 * Define el contrato para la capa de acceso a datos de las incidencias.
 * Esta interfaz abstrae el origen de los datos (local o remoto). Los ViewModels
 * interactúan con esta abstracción, no con la implementación directa.
 */
interface IncidenciaRepository {

    /**
     * Recupera un flujo con todas las incidencias.
     * @return Un [Flow] que emite la lista de incidencias y se actualiza con los cambios.
     */
    fun obtenerTodas(): Flow<List<Incidencia>>

    /**
     * Busca y recupera una única incidencia por su ID.
     * @param id El ID de la incidencia a obtener.
     * @return La [Incidencia] encontrada o `null` si no existe.
     */
    suspend fun getIncidenciaById(id: Int): Incidencia?

    /**
     * Inserta una nueva incidencia en la fuente de datos.
     * @param incidencia El objeto [Incidencia] a guardar.
     */
    suspend fun insertar(incidencia: Incidencia)

    /**
     * Actualiza una incidencia que ya existe.
     * @param incidencia El objeto [Incidencia] con los nuevos datos.
     */
    suspend fun actualizar(incidencia: Incidencia)

    /**
     * Elimina una incidencia de la fuente de datos.
     * @param incidencia El objeto [Incidencia] a eliminar.
     */
    suspend fun eliminar(incidencia: Incidencia)
}
