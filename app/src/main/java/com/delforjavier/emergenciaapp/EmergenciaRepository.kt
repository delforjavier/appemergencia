package com.delforjavier.emergenciaapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.delforjavier.emergenciaapp.RegistroEmergencia

class EmergenciaRepository(private val registroDao: RegistroEmergenciaDao) {

    private fun RegistroEmergenciaEntity.toDomainModel(): RegistroEmergencia {
        return RegistroEmergencia(
            id = this.id,
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

    // Metodo para obtener registros según el tipo de usuario
    fun obtenerRegistrosParaUsuario(usuario: String, esOperador: Boolean): LiveData<List<RegistroEmergencia>> {
        return if (esOperador) {
            // Operadores ven todos los registros
            registroDao.getAllRegistros().map { entidades ->
                entidades.map { it.toDomainModel() }
            }
        } else {
            // Usuarios comunes ven sus propios registros y los de operadores
            registroDao.getRegistrosByUser(usuario).map { entidades ->
                entidades.map { it.toDomainModel() }
            }
        }
    }

    // Métodos existentes...
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