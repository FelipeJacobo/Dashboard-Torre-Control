package com.example.dashboardcobranza.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dashboardcobranza.data.model.ChartData
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.data.model.KPICache
import com.example.dashboardcobranza.data.model.Usuario

@Database(
    entities = [Incidencia::class, Usuario::class, KPICache::class, ChartData::class],
    version = 2, // La versión ya está en 2, lo cual es correcto.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun incidenciaDao(): IncidenciaDao
    abstract fun usuarioDao(): UsuarioDao
    
    // DAOs para las nuevas entidades de caché
    abstract fun kpiCacheDao(): KpiCacheDao
    abstract fun chartDataDao(): ChartDataDao
}
