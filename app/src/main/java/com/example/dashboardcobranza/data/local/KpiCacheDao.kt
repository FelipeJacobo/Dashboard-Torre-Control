package com.example.dashboardcobranza.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dashboardcobranza.data.model.KPICache

/**
 * Data Access Object (DAO) para la entidad KPICache.
 * Define los métodos para interactuar con la tabla de caché de KPIs en la base de datos.
 */
@Dao
interface KpiCacheDao {

    /**
     * Inserta o reemplaza un objeto KPICache en la base de datos.
     * Si ya existe un caché con la misma `cacheKey`, será reemplazado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(kpiCache: KPICache)

    /**
     * Obtiene un objeto KPICache por su clave única.
     * @param key La clave del caché a buscar (ej: "dashboard_stats").
     * @return El objeto KPICache si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM kpi_cache WHERE cacheKey = :key")
    suspend fun getCache(key: String): KPICache?

    /**
     * Elimina una entrada de caché específica por su clave.
     */
    @Query("DELETE FROM kpi_cache WHERE cacheKey = :key")
    suspend fun deleteCache(key: String)

    /**
     * Limpia toda la tabla de caché de KPIs.
     */
    @Query("DELETE FROM kpi_cache")
    suspend fun clearAllCache()
}
