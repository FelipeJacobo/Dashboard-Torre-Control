package com.example.dashboardcobranza.data.local

import androidx.room.*
import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad Incidencia.
 * Define la interfaz con las operaciones de base de datos para las incidencias.
 * Room generará la implementación de este código automáticamente.
 */
@Dao
interface IncidenciaDao {

    /**
     * Recupera todas las incidencias, ordenadas de la más reciente a la más antigua.
     * @return Un Flow que emite la lista de incidencias cada vez que hay un cambio en la tabla.
     */
    @Query("SELECT * FROM tabla_incidencias ORDER BY fechaCreacion DESC")
    fun obtenerTodas(): Flow<List<Incidencia>>

    /**
     * Busca una incidencia específica por su ID.
     * @return La [Incidencia] si se encuentra, de lo contrario `null`.
     */
    @Query("SELECT * FROM tabla_incidencias WHERE id = :id")
    suspend fun getById(id: Int): Incidencia?

    /**
     * Inserta una incidencia. Si ya existe una con el mismo ID, la reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(incidencia: Incidencia)

    /**
     * Actualiza una incidencia existente. Room usa el ID del objeto para encontrar la fila a actualizar.
     */
    @Update
    suspend fun actualizar(incidencia: Incidencia)

    /**
     * Elimina una incidencia de la base de datos.
     */
    @Delete
    suspend fun eliminar(incidencia: Incidencia)

    /**
     * Inserta una lista de incidencias. Útil para poblar la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(incidencias: List<Incidencia>)

    /**
     * Elimina TODAS las incidencias de la tabla. Usar con precaución.
     */
    @Query("DELETE FROM tabla_incidencias")
    suspend fun deleteAll()
}
