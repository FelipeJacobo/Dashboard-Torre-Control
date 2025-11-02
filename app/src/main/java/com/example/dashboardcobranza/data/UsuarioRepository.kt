package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Define el contrato para la capa de acceso a datos de los usuarios.
 * Abstrae el origen de los datos, permitiendo que en el futuro se pueda cambiar la implementación
 * (ej. a una que obtenga datos de una API remota) sin afectar a los ViewModels.
 */
interface UsuarioRepository {

    /**
     * Busca un usuario por su dirección de email para el proceso de login.
     */
    suspend fun buscarPorEmail(email: String): Usuario?

    /**
     * Obtiene un usuario por su ID como un [Flow] para observar cambios en tiempo real.
     */
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario?>

    /**
     * Inserta un nuevo usuario en la fuente de datos.
     */
    suspend fun insertar(usuario: Usuario): Long

    /**
     * Actualiza un usuario existente.
     */
    suspend fun actualizarUsuario(usuario: Usuario)

    /**
     * Obtiene un [Flow] con la lista de todos los usuarios registrados.
     */
    fun obtenerTodosLosUsuarios(): Flow<List<Usuario>>
}
