package com.example.dashboardcobranza.data.local

import androidx.room.*
import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad [Incidencia].
 * Define la interfaz para interactuar con la tabla `incidencias`.
 */
@Dao
interface IncidenciaDao {

    /**
     * Recupera un [Flow] con todas las incidencias, ordenadas de la más reciente a la más antigua.
     * Al devolver un Flow, la UI se actualizará automáticamente ante cualquier cambio.
     */
    @Query("SELECT * FROM incidencias ORDER BY fecha_creacion DESC")
    fun obtenerTodas(): Flow<List<Incidencia>>

    /**
     * Busca una incidencia específica por su ID y la devuelve como un [Flow].
     */
    @Query("SELECT * FROM incidencias WHERE id = :id")
    fun getById(id: Int): Flow<Incidencia?>

    /**
     * Inserta una incidencia. Si ya existe una con el mismo ID, la reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(incidencia: Incidencia)

    /**
     * Elimina una incidencia de la base de datos.
     */
    @Delete
    suspend fun eliminar(incidencia: Incidencia)
}
