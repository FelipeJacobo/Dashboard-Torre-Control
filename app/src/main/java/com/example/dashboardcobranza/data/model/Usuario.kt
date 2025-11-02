package com.example.dashboardcobranza.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Define la entidad (tabla) `usuarios` en la base de datos Room.
 * El índice en el campo `email` asegura que las búsquedas por este campo sean rápidas
 * y que no se puedan crear dos usuarios con el mismo email.
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    /**
     * Clave primaria autoincremental.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Nombre completo del usuario.
     */
    val nombre: String,

    /**
     * Dirección de correo electrónico. Se usa como identificador único para el login.
     */
    val email: String,

    /**
     * Contraseña del usuario. En una app real, esto debería almacenarse como un hash.
     */
    val contrasena: String,

    /**
     * Rol del usuario. `true` si es Administrador, `false` si es un usuario normal.
     */
    val esAdmin: Boolean = false,

    /**
     * Número de empleado del usuario.
     */
    val numeroEmpleado: String,

    /**
     * Puesto o cargo del usuario dentro de la empresa.
     */
    val puesto: String,

    /**
     * Nombre de la empresa a la que pertenece el usuario.
     */
    val empresa: String,

    /**
     * Ciudad donde se ubica el usuario.
     */
    val ciudad: String,

    /**
     * Timestamp de la fecha de creación del registro.
     */
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * Timestamp de la última actualización del registro.
     */
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    val updatedAt: Long = System.currentTimeMillis()
)
