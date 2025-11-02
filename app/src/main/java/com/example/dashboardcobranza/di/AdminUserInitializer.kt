package com.example.dashboardcobranza.di

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dashboardcobranza.data.local.UsuarioDao
import com.example.dashboardcobranza.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * Se encarga de pre-poblar la base de datos con un usuario administrador la primera vez que se crea.
 */
class AdminUserInitializer(
    // Usamos Provider<> para evitar un ciclo de dependencias durante la creación de la BD.
    private val usuarioDaoProvider: Provider<UsuarioDao>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    /**
     * Este método se llama UNA SOLA VEZ, cuando la base de datos se crea por primera vez.
     */
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            populateDatabase()
        }
    }

    /**
     * Inserta el usuario administrador por defecto.
     */
    private suspend fun populateDatabase() {
        val adminUser = Usuario(
            nombre = "Admin Coppel",
            email = "admin@coppel.com",
            contrasena = "admin123", // ¡IMPORTANTE! Esto es solo para desarrollo. Nunca en producción.
            esAdmin = true,
            numeroEmpleado = "00001",
            puesto = "Torre de Control",
            empresa = "Coppel S.A. de C.V.",
            ciudad = "Culiacán"
        )
        // Obtenemos el DAO usando el provider e insertamos el usuario.
        usuarioDaoProvider.get().insertar(adminUser)
    }
}
