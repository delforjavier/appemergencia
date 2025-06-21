package com.delforjavier.emergenciaapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.delforjavier.emergenciaapp.RegistroEmergencia

class EmergenciaRepository(private val registroDao: RegistroEmergenciaDao) {

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

    // Métodos con LiveData
    fun obtenerRegistrosPorUsuario(usuario: String): LiveData<List<RegistroEmergencia>> {
        return registroDao.getRegistrosByUser(usuario).map { entidades ->
            entidades.map { it.toDomainModel() }
        }
    }

    fun obtenerTodosRegistros(): LiveData<List<RegistroEmergencia>> {
        return registroDao.getAllRegistros().map { entidades ->
            entidades.map { it.toDomainModel() }
        }
    }

    // Métodos suspend (sin cambios)
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