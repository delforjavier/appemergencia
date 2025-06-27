package com.delforjavier.emergenciaapp.data
// Define el paquete donde está este archivo, dentro de la carpeta 'data' de la app.



import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.delforjavier.emergenciaapp.RegistroEmergencia
// Importa clases necesarias: LiveData y map para transformaciones reactivas, y el modelo de dominio RegistroEmergencia.



class EmergenciaRepository(private val registroDao: RegistroEmergenciaDao) {
    // Clase repositorio que centraliza el acceso a los datos relacionados a emergencias.
    // Recibe como parámetro el DAO para acceder a la base de datos.


    private fun RegistroEmergenciaEntity.toDomainModel(): RegistroEmergencia {
        // Función privada de extensión que convierte una entidad de la base de datos (RegistroEmergenciaEntity)
        // a un modelo de dominio (RegistroEmergencia), que es el que se usa en la app.


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

    // Metodo público que devuelve los registros según el tipo de usuario (operador o común)

    fun obtenerRegistrosParaUsuario(usuario: String, esOperador: Boolean): LiveData<List<RegistroEmergencia>> {
        return if (esOperador) {
            // Si es operador, obtiene todos los registros de la base de datos
            registroDao.getAllRegistros().map { entidades ->
                entidades.map { it.toDomainModel() }  // Convierte cada entidad a modelo de dominio
            }
        } else {
            // Si no es operador, solo obtiene los registros creados por ese usuario
            registroDao.getRegistrosByUser(usuario).map { entidades ->
                entidades.map { it.toDomainModel() } // También convierte a modelo de dominio
            }
        }
    }

    // Inserta un nuevo registro de emergencia en la base de datos

    suspend fun insertarRegistro(registro: RegistroEmergenciaEntity): Long {
        return registroDao.insertRegistro(registro)
    }

    // Actualiza un registro existente

    suspend fun actualizarRegistro(registro: RegistroEmergenciaEntity) {
        registroDao.updateRegistro(registro)
    }


    // Elimina un registro específico

    suspend fun eliminarRegistro(registro: RegistroEmergenciaEntity) {
        registroDao.deleteRegistro(registro)
    }

    // Elimina todos los registros creados por un usuario determinado

    suspend fun eliminarRegistrosPorUsuario(usuario: String) {
        registroDao.deleteRegistrosByUser(usuario)
    }


    // Elimina todos los registros de la base de datos

    suspend fun eliminarTodosRegistros() {
        registroDao.deleteAllRegistros()
    }
}