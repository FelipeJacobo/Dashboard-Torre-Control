package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.local.UsuarioDao
import com.example.dashboardcobranza.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación concreta de [UsuarioRepository] que opera exclusivamente
 * con la base de datos local a través de [UsuarioDao].
 *
 * @param usuarioDao El DAO para la tabla de usuarios, inyectado por Hilt.
 */
@Singleton
class OfflineUsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    override suspend fun buscarPorEmail(email: String): Usuario? {
        return usuarioDao.buscarPorEmail(email)
    }

    override fun obtenerUsuarioPorId(id: Int): Flow<Usuario?> {
        return usuarioDao.buscarPorId(id)
    }

    override suspend fun insertar(usuario: Usuario): Long {
        return usuarioDao.insertar(usuario)
    }
    
    override suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizar(usuario)
    }

    override fun obtenerTodosLosUsuarios(): Flow<List<Usuario>> {
        return usuarioDao.obtenerTodos()
    }
}
