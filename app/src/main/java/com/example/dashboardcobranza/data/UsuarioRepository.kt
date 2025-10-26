package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.model.Usuario

/**
 * Define el contrato para la capa de acceso a datos de los usuarios.
 * Esta interfaz abstrae el origen de los datos. Los ViewModels interactúan
 * con esta abstracción en lugar de una implementación concreta.
 */
interface UsuarioRepository {

    /**
     * Busca un usuario por su dirección de email.
     * @param email El email del usuario a buscar.
     * @return El objeto [Usuario] si se encuentra, o `null` si no.
     */
    suspend fun buscarPorEmail(email: String): Usuario?

    /**
     * Intenta registrar un nuevo usuario en la fuente de datos.
     * @param usuario El objeto [Usuario] con los datos para el registro.
     * @return `true` si el registro fue exitoso, `false` si falló (ej. email ya en uso).
     */
    suspend fun registrarUsuario(usuario: Usuario): Boolean

    // --- INICIO DE LA CORRECCIÓN ---
    /**
     * Busca un usuario por su ID.
     * @param id El ID del usuario a buscar.
     * @return El objeto [Usuario] si se encuentra, o `null` si no.
     */
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?
    // --- FIN DE LA CORRECCIÓN ---
}
