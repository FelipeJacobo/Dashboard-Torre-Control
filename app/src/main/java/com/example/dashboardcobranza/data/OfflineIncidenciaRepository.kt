package com.example.dashboardcobranza.data

import com.example.dashboardcobranza.data.local.IncidenciaDao
import com.example.dashboardcobranza.data.model.Incidencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementación concreta de [IncidenciaRepository] que opera con la base de datos local (Room).
 * Su única responsabilidad es actuar como intermediario, delegando cada operación al [IncidenciaDao].
 *
 * @param incidenciaDao El DAO inyectado por Hilt para el acceso a la base de datos.
 */
class OfflineIncidenciaRepository @Inject constructor(
    private val incidenciaDao: IncidenciaDao
) : IncidenciaRepository {

    /**
     * Delega la obtención de todas las incidencias al DAO.
     */
    override fun obtenerTodas(): Flow<List<Incidencia>> {
        return incidenciaDao.obtenerTodas()
    }

    /**
     * Delega la búsqueda de una incidencia por su ID al DAO.
     */
    override suspend fun getIncidenciaById(id: Int): Incidencia? {
        return incidenciaDao.getById(id)
    }

    /**
     * Delega la inserción de una incidencia al DAO.
     */
    override suspend fun insertar(incidencia: Incidencia) {
        incidenciaDao.insertar(incidencia)
    }

    /**
     * Delega la actualización de una incidencia al DAO.
     */
    override suspend fun actualizar(incidencia: Incidencia) {
        incidenciaDao.actualizar(incidencia)
    }

    /**
     * Delega la eliminación de una incidencia al DAO.
     */
    override suspend fun eliminar(incidencia: Incidencia) {
        incidenciaDao.eliminar(incidencia)
    }
}
