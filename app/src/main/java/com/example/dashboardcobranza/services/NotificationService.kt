package com.example.dashboardcobranza.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.dashboardcobranza.CobranzaApplication
import com.example.dashboardcobranza.MainActivity
import com.example.dashboardcobranza.data.model.Incidencia
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNewIncidenciaNotification(incidencia: Incidencia) {
        // Creamos un Intent para abrir la MainActivity cuando se toque la notificación.
        val intent = Intent(context, MainActivity::class.java).apply {
            // TODO: Añadir extras al intent para navegar directamente al detalle de la incidencia.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 
            incidencia.id, // Usamos el ID de la incidencia como request code
            intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // SOLUCIÓN: Se usa un icono por defecto de Android para evitar el error de compilación.
        // TODO: Reemplazar `android.R.drawable.ic_dialog_info` con un icono personalizado en `res/drawable`.
        val notification = NotificationCompat.Builder(context, CobranzaApplication.INCIDENCIA_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nueva Incidencia Asignada")
            .setContentText("Se te ha asignado la incidencia: #${incidencia.id} - ${incidencia.titulo}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Prioridad: ${incidencia.prioridad}. Toca para ver los detalles."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // La notificación se cierra al tocarla
            .build()

        notificationManager.notify(incidencia.id, notification)
    }
}
