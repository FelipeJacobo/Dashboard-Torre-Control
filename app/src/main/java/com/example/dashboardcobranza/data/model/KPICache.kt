package com.example.dashboardcobranza.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para almacenar en caché los resultados de los KPIs calculados.
 * Esto evita tener que recalcularlos constantemente, mejorando el rendimiento.
 */
@Entity(tableName = "kpi_cache")
data class KPICache(
    // Usamos un String como clave para poder identificar fácilmente el KPI cacheado (ej: "dashboard_stats")
    @PrimaryKey
    val cacheKey: String,

    // El contenido del caché, almacenado como un String en formato JSON.
    val jsonData: String,

    // Timestamp de cuándo se creó el caché para poder invalidarlo después de un tiempo (TTL - Time To Live).
    val timestamp: Long = System.currentTimeMillis()
)
