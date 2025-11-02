package com.example.dashboardcobranza.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // PASO 1: Crear la nueva tabla de usuarios con el esquema correcto
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `usuarios` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `nombre` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `contrasena` TEXT NOT NULL,
                    `esAdmin` INTEGER NOT NULL DEFAULT 0,
                    `numeroEmpleado` TEXT NOT NULL,
                    `puesto` TEXT NOT NULL,
                    `empresa` TEXT NOT NULL,
                    `ciudad` TEXT NOT NULL,
                    `created_at` INTEGER NOT NULL DEFAULT 0,
                    `updated_at` INTEGER NOT NULL DEFAULT 0
                )
            """)
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_usuarios_email` ON `usuarios` (`email`)")

            // PASO 2: Añadir las nuevas columnas a la tabla de incidencias
            db.execSQL("ALTER TABLE `tabla_incidencias` ADD COLUMN `assignedTo` TEXT")
            db.execSQL("ALTER TABLE `tabla_incidencias` ADD COLUMN `updated_at` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `tabla_incidencias` ADD COLUMN `is_synced` INTEGER NOT NULL DEFAULT 0")

            // PASO 3: Renombrar la tabla de incidencias al nuevo nombre
            db.execSQL("ALTER TABLE `tabla_incidencias` RENAME TO `incidencias`")

            // PASO 4: Crear las nuevas tablas de caché
            db.execSQL("CREATE TABLE IF NOT EXISTS `kpi_cache` (`cacheKey` TEXT NOT NULL, `jsonData` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`cacheKey`))")
            db.execSQL("CREATE TABLE IF NOT EXISTS `chart_data_cache` (`chartId` TEXT NOT NULL, `chartDataJson` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`chartId`))")
        }
    }
}
