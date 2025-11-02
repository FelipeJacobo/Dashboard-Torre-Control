package com.example.dashboardcobranza.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Conversores de tipos para la base de datos Room.
 * Room los utiliza para saber cómo guardar y leer tipos de datos complejos que no soporta de forma nativa.
 */
class Converters {

    /**
     * Convierte un Timestamp (Long) de la base de datos a un objeto Date.
     * Room llamará a esta función cuando lea datos de la BD.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Convierte un objeto Date a un Timestamp (Long) para guardarlo en la base de datos.
     * Room llamará a esta función cuando escriba datos en la BD.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Aquí se podrían añadir más conversores en el futuro, como por ejemplo:
    // - De una lista de Strings a un único String en formato JSON y viceversa.
    // - De un Enum a su valor de String.
}
