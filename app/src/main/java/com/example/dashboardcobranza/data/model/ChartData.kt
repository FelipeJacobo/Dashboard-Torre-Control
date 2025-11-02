package com.example.dashboardcobranza.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para almacenar en caché los datos procesados para las gráficas.
 * Similar a KPICache, pero dedicada a los datos de visualización.
 */
@Entity(tableName = "chart_data_cache")
data class ChartData(
    @PrimaryKey
    val chartId: String, // ej: "monthly_performance_chart"
    val chartDataJson: String, // Datos de la gráfica en formato JSON
    val timestamp: Long = System.currentTimeMillis()
)
