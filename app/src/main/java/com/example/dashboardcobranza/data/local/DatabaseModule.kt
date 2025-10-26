package com.example.dashboardcobranza.data.local

import android.content.Context
import androidx.room.Room
import com.example.dashboardcobranza.data.IncidenciaRepository
import com.example.dashboardcobranza.data.OfflineIncidenciaRepository
import com.example.dashboardcobranza.data.OfflineUsuarioRepository
import com.example.dashboardcobranza.data.UsuarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt que define cómo crear y proveer las dependencias
 * relacionadas con la base de datos (Room) y los repositorios.
 * Hilt leerá este "manual de instrucciones" para saber cómo inyectar
 * la base de datos, los DAOs y los Repositorios donde se necesiten.
 */
@Module
@InstallIn(SingletonComponent::class) // Esto hace que las dependencias vivan mientras la app esté viva (Singleton).
object DatabaseModule {

    /**
     * Provee la instancia única de la base de datos [AppDatabase] para toda la aplicación.
     * La anotación @Singleton asegura que solo se cree una vez.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dashboard_database" // Nombre del archivo de la base de datos.
        )
            // En una app de producción, aquí manejarías migraciones.
            // Esto simplemente borra y recrea la BD si el esquema cambia. Útil para desarrollo.
            .fallbackToDestructiveMigration()
            .build()
    }

    // --- Dependencias para Incidencias ---

    /**
     * Provee el DAO para las incidencias a partir de la instancia de la base de datos.
     */
    @Provides
    fun provideIncidenciaDao(database: AppDatabase): IncidenciaDao {
        return database.incidenciaDao()
    }

    /**
     * Provee la implementación del repositorio de incidencias.
     * Cuando un ViewModel pida un [IncidenciaRepository], Hilt sabrá que debe
     * crear un [OfflineIncidenciaRepository] y entregarlo.
     */
    @Provides
    @Singleton
    fun provideIncidenciaRepository(incidenciaDao: IncidenciaDao): IncidenciaRepository {
        return OfflineIncidenciaRepository(incidenciaDao)
    }

    // --- Dependencias para Usuarios ---

    /**
     * Provee el DAO para los usuarios, extraído desde la instancia de la base de datos.
     */
    @Provides
    fun provideUsuarioDao(database: AppDatabase): UsuarioDao {
        return database.usuarioDao()
    }

    /**
     * Provee la implementación del repositorio de usuarios.
     * Le dice a Hilt que, para la interfaz [UsuarioRepository], debe usar la clase
     * [OfflineUsuarioRepository].
     */
    @Provides
    @Singleton
    fun provideUsuarioRepository(usuarioDao: UsuarioDao): UsuarioRepository {
        return OfflineUsuarioRepository(usuarioDao)
    }
}
