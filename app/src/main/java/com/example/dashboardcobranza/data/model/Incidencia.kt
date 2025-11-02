package com.example.dashboardcobranza.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define la entidad (tabla) `incidencias` en la base de datos Room.
 */
@Entity(tableName = "incidencias")
data class Incidencia(
    /**
     * Clave primaria autoincremental para la incidencia.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Título corto y descriptivo de la incidencia.
     */
    val titulo: String,

    /**
     * Descripción detallada de la incidencia.
     */
    val descripcion: String,

    /**
     * Nivel de urgencia o impacto de la incidencia (ej. "Baja", "Media", "Alta", "Crítica").
     */
    val prioridad: String,

    /**
     * El estado actual del ciclo de vida de la incidencia (ej. "Nueva", "En Progreso", "Cerrada").
     */
    val estado: String,

    /**
     * El nombre del usuario al que se le ha asignado la incidencia. Puede ser nulo.
     */
    val assignedTo: String? = null,

    /**
     * Timestamp de la fecha de creación de la incidencia.
     */
    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis(),

    /**
     * Timestamp de la última vez que se actualizó la incidencia.
     */
    @ColumnInfo(name = "updated_at", defaultValue = "0")
    val updatedAt: Long = 0,

    /**
     * Bandera para la sincronización con un backend. `true` si ya está sincronizada.
     */
    @ColumnInfo(name = "is_synced", defaultValue = "0")
    val isSynced: Boolean = false
)
