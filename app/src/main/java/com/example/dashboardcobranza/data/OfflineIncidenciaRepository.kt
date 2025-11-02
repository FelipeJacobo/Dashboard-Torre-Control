package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.local.IncidenciaDao
import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación concreta de [IncidenciaRepository] que opera exclusivamente
 * con la base de datos local a través de [IncidenciaDao].
 *
 * @param incidenciaDao El DAO para la tabla de incidencias, inyectado por Hilt.
 */
@Singleton
class OfflineIncidenciaRepository @Inject constructor(
    private val incidenciaDao: IncidenciaDao
) : IncidenciaRepository {

    override fun obtenerTodas(): Flow<List<Incidencia>> {
        return incidenciaDao.obtenerTodas()
    }

    override fun getIncidenciaById(id: Int): Flow<Incidencia?> {
        return incidenciaDao.getById(id)
    }

    override suspend fun insertar(incidencia: Incidencia) {
        incidenciaDao.insertar(incidencia)
    }

    override suspend fun eliminar(incidencia: Incidencia) {
        incidenciaDao.eliminar(incidencia)
    }
}
