package com.example.dashboardcobranza.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.dashboardcobranza.data.IncidenciaRepository
import com.example.dashboardcobranza.data.OfflineIncidenciaRepository
import com.example.dashboardcobranza.data.OfflineUsuarioRepository
import com.example.dashboardcobranza.data.UsuarioRepository
import com.example.dashboardcobranza.data.database.AdminUserInitializer
import com.example.dashboardcobranza.data.database.AppDatabase
import com.example.dashboardcobranza.data.database.DatabaseMigrations
import com.example.dashboardcobranza.data.local.ChartDataDao
import com.example.dashboardcobranza.data.local.IncidenciaDao
import com.example.dashboardcobranza.data.local.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        usuarioDaoProvider: Provider<UsuarioDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "cobranza_database"
        )
        .addMigrations(DatabaseMigrations.MIGRATION_1_2)
        .addCallback(AdminUserInitializer(usuarioDaoProvider))
        .build()
    }

    @Provides
    fun provideUsuarioDao(db: AppDatabase): UsuarioDao = db.usuarioDao()

    @Provides
    fun provideIncidenciaDao(db: AppDatabase): IncidenciaDao = db.incidenciaDao()

    @Provides
    fun provideChartDataDao(db: AppDatabase): ChartDataDao = db.chartDataDao()

    @Provides
    @Singleton
    fun provideUsuarioRepository(usuarioDao: UsuarioDao): UsuarioRepository {
        return OfflineUsuarioRepository(usuarioDao)
    }

    @Provides
    @Singleton
    fun provideIncidenciaRepository(incidenciaDao: IncidenciaDao): IncidenciaRepository {
        return OfflineIncidenciaRepository(incidenciaDao)
    }

}
