package com.example.dashboardcobranza.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// --- INICIO DE LA ACTUALIZACIÓN ---

@Entity(tableName = "tabla_incidencias")
data class Incidencia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 1. Se añade un campo 'titulo' para un resumen corto de la incidencia.
    val titulo: String,

    // El campo 'descripcion' se mantiene para los detalles completos.
    val descripcion: String,

    // 2. Se añade un campo 'prioridad' para poder categorizar.
    val prioridad: String,

    // El campo 'estado' se mantiene.
    val estado: String,

    // 3. Se añade 'fechaCreacion' para saber cuándo se registró.
    //    Usamos System.currentTimeMillis() para establecer la fecha y hora actual
    //    por defecto cuando se crea un nuevo objeto.
    val fechaCreacion: Long = System.currentTimeMillis()
)
// --- FIN DE LA ACTUALIZACIÓN ---
