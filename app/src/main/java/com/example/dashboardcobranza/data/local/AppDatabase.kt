package com.example.dashboardcobranza.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.model.Usuario

@Database(entities = [Incidencia::class, Usuario::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Esta función para el IncidenciaDao
    abstract fun incidenciaDao(): IncidenciaDao

    //Añadimos la función abstracta para nuestro UsuarioDao
    abstract fun usuarioDao(): UsuarioDao
}
