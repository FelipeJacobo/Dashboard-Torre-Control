package com.example.dashboardcobranza.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa la tabla de usuarios en la base de datos.
 *
 * @param id El identificador único autogenerado para el usuario.
 * @param nombre El nombre del usuario.
 * @param email El correo electrónico del usuario, que debe ser único.
 * @param contrasena La contraseña del usuario. (En una app real, esto debería estar hasheado).
 * @param esAdmin Un booleano que es 'true' si el usuario tiene privilegios de administrador.
 */
@Entity(
    tableName = "tabla_usuarios",
    // Nos aseguramos de que el email sea único para evitar registros duplicados.
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val email: String,
    // ¡Importante! En una aplicación de producción, NUNCA guardes
    // contraseñas en texto plano. Deberías guardar un "hash" de la contraseña.
    val contrasena: String,

    // --- INICIO DE LA ACTUALIZACIÓN ---
    val esAdmin: Boolean = false // Por defecto, ningún usuario es administrador.
    // --- FIN DE LA ACTUALIZACIÓN ---
)
