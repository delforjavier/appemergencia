package com.delforjavier.emergenciaapp.data

import com.delforjavier.emergenciaapp.RegistroEmergencia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmergenciaRepository(private val registroDao: RegistroEmergenciaDao) {

    // Función de extensión para conversión
    private fun RegistroEmergenciaEntity.toDomainModel(): RegistroEmergencia {
        return RegistroEmergencia(
            nombre = this.nombre,
            apellido = this.apellido,
            domicilio = this.domicilio,
            cantidadAdultos = this.cantidadAdultos,
            cantidadMayores = this.cantidadMayores,
            cantidadNinos = this.cantidadNinos,
            observaciones = this.observaciones,
            tratamientoMedico = this.tratamientoMedico,
            creador = this.creador
        )
    }

    // Operaciones con Flow
    fun obtenerRegistrosPorUsuario(usuario: String): Flow<List<RegistroEmergencia>> {
        val flowDeEntidades: Flow<List<RegistroEmergenciaEntity>> = registroDao.getRegistrosByUser(usuario)
        return flowDeEntidades.map { entidades ->
            entidades.map { entidad ->
                RegistroEmergencia(
                    nombre = entidad.nombre,
                    apellido = entidad.apellido,
                    domicilio = entidad.domicilio,
                    cantidadAdultos = entidad.cantidadAdultos,
                    cantidadMayores = entidad.cantidadMayores,
                    cantidadNinos = entidad.cantidadNinos,
                    observaciones = entidad.observaciones,
                    tratamientoMedico = entidad.tratamientoMedico,
                    creador = entidad.creador
                )
            }
        }
    }

    fun obtenerTodosRegistros(): Flow<List<RegistroEmergencia>> {
        val flowDeEntidades: Flow<List<RegistroEmergenciaEntity>> = registroDao.getAllRegistros()
        return flowDeEntidades.map { entidades ->
            entidades.map { entidad ->
                RegistroEmergencia(
                    nombre = entidad.nombre,
                    apellido = entidad.apellido,
                    domicilio = entidad.domicilio,
                    cantidadAdultos = entidad.cantidadAdultos,
                    cantidadMayores = entidad.cantidadMayores,
                    cantidadNinos = entidad.cantidadNinos,
                    observaciones = entidad.observaciones,
                    tratamientoMedico = entidad.tratamientoMedico,
                    creador = entidad.creador
                )
            }
        }
    }

    // Operaciones suspendidas
    suspend fun insertarRegistro(registro: RegistroEmergenciaEntity): Long {
        return registroDao.insertRegistro(registro)
    }

    suspend fun actualizarRegistro(registro: RegistroEmergenciaEntity) {
        registroDao.updateRegistro(registro)
    }

    suspend fun eliminarRegistro(registro: RegistroEmergenciaEntity) {
        registroDao.deleteRegistro(registro)
    }

    suspend fun eliminarRegistrosPorUsuario(usuario: String) {
        registroDao.deleteRegistrosByUser(usuario)
    }

    suspend fun eliminarTodosRegistros() {
        registroDao.deleteAllRegistros()
    }
}