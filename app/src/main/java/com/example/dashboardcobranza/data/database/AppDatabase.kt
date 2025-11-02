package com.example.dashboardcobranza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dashboardcobranza.data.local.ChartDataDao
import com.example.dashboardcobranza.data.local.IncidenciaDao
import com.example.dashboardcobranza.data.local.UsuarioDao
import com.example.dashboardcobranza.data.model.ChartData
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * Define la clase principal de la base de datos Room para la aplicación.
 * @property entities Un array de todas las clases de entidad que forman parte de esta base de datos.
 * @property version El número de versión de la base de datos. Debe incrementarse en cada cambio de esquema.
 */
@Database(
    entities = [Usuario::class, Incidencia::class, ChartData::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun incidenciaDao(): IncidenciaDao
    abstract fun chartDataDao(): ChartDataDao
}

/**
 * Callback que se ejecuta cuando la base de datos es creada por primera vez.
 * Se utiliza para pre-poblar la base de datos con datos iniciales, como el usuario administrador.
 * @param usuarioDaoProvider Un [Provider] del DAO para romper la dependencia cíclica con el AppModule.
 */
class AdminUserInitializer(private val usuarioDaoProvider: Provider<UsuarioDao>) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val usuarioDao = usuarioDaoProvider.get()
            val adminUser = Usuario(
                id = 1,
                nombre = "Admin Coppel",
                email = "admin@coppel.com",
                contrasena = "admin123",
                esAdmin = true,
                numeroEmpleado = "00000",
                puesto = "Administrador de Sistema",
                empresa = "Coppel S.A. de C.V.",
                ciudad = "Culiacán"
            )
            usuarioDao.insertar(adminUser)
        }
    }
}

// SOLUCIÓN: El objeto DatabaseMigrations se ha movido a su propio archivo para evitar la redeclaración.
