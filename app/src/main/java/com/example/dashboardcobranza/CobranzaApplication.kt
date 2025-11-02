package com.example.dashboardcobranza

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase principal de la aplicación, anotada con @HiltAndroidApp para inicializar Hilt.
 * Es el punto de entrada que el sistema Android usa para crear el proceso de la app.
 */
@HiltAndroidApp
class CobranzaApplication : Application() {

    companion object {
        /**
         * ID único para el canal de notificaciones de incidencias.
         * Es público para que los servicios de notificaciones puedan usarlo.
         */
        const val INCIDENCIA_CHANNEL_ID = "incidencias_channel"
    }

    /**
     * Se llama cuando la aplicación es creada.
     * Ideal para inicializaciones que deben ocurrir una sola vez.
     */
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * Crea el canal de notificaciones para la app.
     * En versiones de Android 8.0 (API 26) o superior, todas las notificaciones deben
     * pertenecer a un canal, que el usuario puede gestionar desde los ajustes del sistema.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Asignación de Incidencias"
            val descriptionText = "Notificaciones cuando se te asigna una nueva incidencia."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(INCIDENCIA_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
