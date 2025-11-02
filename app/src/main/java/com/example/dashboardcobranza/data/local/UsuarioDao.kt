package com.example.dashboardcobranza.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dashboardcobranza.data.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad [Usuario].
 * Define la interfaz para interactuar con la tabla `usuarios` en la base de datos.
 */
@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario. Si el email ya existe, la operación se aborta
     * gracias al índice único en la entidad.
     * @return El ID de la fila recién insertada.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    /**
     * Actualiza un usuario existente en la base de datos.
     */
    @Update
    suspend fun actualizar(usuario: Usuario)

    /**
     * Busca un usuario por su dirección de email. Es una función suspendida porque
     * es una operación de una sola vez.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    /**
     * Busca un usuario por su ID y devuelve un [Flow].
     * Esto permite a la UI observar cambios en el perfil del usuario en tiempo real.
     */
    @Query("SELECT * FROM usuarios WHERE id = :userId LIMIT 1")
    fun buscarPorId(userId: Int): Flow<Usuario?>

    /**
     * Obtiene un [Flow] con la lista de todos los usuarios, ordenados por nombre.
     */
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<Usuario>>
}
