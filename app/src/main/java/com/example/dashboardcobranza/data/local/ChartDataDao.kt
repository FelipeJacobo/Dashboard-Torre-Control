package com.example.dashboardcobranza.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dashboardcobranza.data.model.ChartData

/**
 * Data Access Object (DAO) para la entidad ChartData.
 * Define los métodos para interactuar con la tabla de caché de datos de gráficas.
 */
@Dao
interface ChartDataDao {

    /**
     * Inserta o reemplaza los datos de una gráfica en el caché.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartData(chartData: ChartData)

    /**
     * Obtiene los datos de una gráfica por su ID.
     * @param chartId El ID de la gráfica a buscar (ej: "monthly_performance_chart").
     * @return El objeto ChartData si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM chart_data_cache WHERE chartId = :chartId")
    suspend fun getChartData(chartId: String): ChartData?

    /**
     * Limpia toda la tabla de caché de datos de gráficas.
     */
    @Query("DELETE FROM chart_data_cache")
    suspend fun clearAllChartData()
}
