package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.local.UsuarioDao
import com.example.dashboardcobranza.data.model.Usuario
import javax.inject.Inject

/**
 * Implementación concreta de [UsuarioRepository] que opera con la base de datos local (Room).
 * Encapsula la lógica de negocio relacionada con las operaciones de usuario.
 *
 * @param usuarioDao El DAO inyectado por Hilt para el acceso a la base de datos.
 */
class OfflineUsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    /**
     * Delega la búsqueda de un usuario por su email directamente al DAO.
     */
    override suspend fun buscarPorEmail(email: String): Usuario? {
        return usuarioDao.buscarPorEmail(email)
    }

    /**
     * Gestiona el registro de un nuevo usuario.
     * @return `true` si el usuario fue registrado con éxito, `false` si hubo un error
     * (por ejemplo, el email ya existía).
     */
    override suspend fun registrarUsuario(usuario: Usuario): Boolean {
        // La función `insertar` del DAO devuelve el ID de la nueva fila, o -1 si falla
        // (ej. por conflicto de email duplicado). Comparamos si es mayor que -1
        // para determinar si la operación fue exitosa.
        return usuarioDao.insertar(usuario) > -1
    }

    // --- INICIO DE LA CORRECCIÓN ---
    /**
     * Implementación del método faltante.
     * Delega la búsqueda de un usuario por su ID directamente al DAO.
     */
    override suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        // Llama a la función que ya hemos definido en UsuarioDao
        return usuarioDao.buscarPorId(id)
    }
    // --- FIN DE LA CORRECCIÓN ---
}
