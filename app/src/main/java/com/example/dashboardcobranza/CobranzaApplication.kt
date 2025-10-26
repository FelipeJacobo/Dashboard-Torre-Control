package com.example.dashboardcobranza

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * El punto de entrada principal para toda la aplicación.
 * Android crea una instancia de esta clase cuando la app se inicia.
 */
// Esta anotación es el corazón de Hilt.
// Le indica al compilador que aquí comienza la generación de código
// para la inyección de dependencias. Sin esto, nada de Hilt funcionaría.
@HiltAndroidApp
class CobranzaApplication : Application()
