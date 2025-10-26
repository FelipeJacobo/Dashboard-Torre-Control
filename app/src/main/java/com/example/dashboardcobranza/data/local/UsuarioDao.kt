package com.example.dashboardcobranza.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dashboardcobranza.data.model.Usuario

/**
 * DAO (Data Access Object) para la entidad Usuario.
 * Define las operaciones de base de datos permitidas sobre la tabla de usuarios.
 */
@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario.
     * La estrategia `OnConflictStrategy.ABORT` asegura que la operación se cancele
     * si se intenta insertar un email que ya existe (gracias al índice único en la entidad).
     * @param usuario El objeto [Usuario] a registrar.
     * @return El ID de la fila creada, o -1 si se abortó la operación (ej. email duplicado).
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    /**
     * Busca un usuario por su dirección de email.
     * @param email El email del usuario a buscar.
     * @return El objeto [Usuario] si se encuentra, o `null` si no existe.
     */
    @Query("SELECT * FROM tabla_usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    // --- INICIO DE LA CORRECCIÓN ---
    /**
     * Busca un usuario por su ID.
     * @param userId El ID del usuario a buscar.
     * @return El objeto [Usuario] si se encuentra, o `null` si no existe.
     */
    @Query("SELECT * FROM tabla_usuarios WHERE id = :userId LIMIT 1")
    suspend fun buscarPorId(userId: Int): Usuario?
    // --- FIN DE LA CORRECCIÓN ---
}
