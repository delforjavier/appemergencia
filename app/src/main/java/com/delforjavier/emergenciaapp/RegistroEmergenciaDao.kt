package com.delforjavier.emergenciaapp.data
// Define el paquete donde se encuentra este archivo. En este caso, dentro de la carpeta 'data' de la app.


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
// Importa las clases necesarias de Room y LiveData para definir el DAO (Data Access Object).



@Dao
interface RegistroEmergenciaDao {
    // Indica que esta interfaz es un DAO de Room, es decir, se usa para acceder a la base de datos.


    @Insert
    suspend fun insertRegistro(registro: RegistroEmergenciaEntity): Long
    // Inserta un nuevo registro de emergencia en la base de datos.
    // La función es 'suspend' porque se ejecuta en una corrutina (fuera del hilo principal).
    // Devuelve un Long que representa el ID del nuevo registro insertado.



    @Update
    suspend fun updateRegistro(registro: RegistroEmergenciaEntity)
    // Actualiza un registro de emergencia existente en la base de datos.



    @Delete
    suspend fun deleteRegistro(registro: RegistroEmergenciaEntity)
    // Elimina un registro específico de emergencia de la base de datos.




    @Query("SELECT * FROM registros_emergencia WHERE creador = :usuario")
    fun getRegistrosByUser(usuario: String): LiveData<List<RegistroEmergenciaEntity>>
    // Obtiene todos los registros de emergencia creados por un usuario específico.
    // El resultado es un LiveData que se actualiza automáticamente si los datos cambian.



    @Query("SELECT * FROM registros_emergencia")
    fun getAllRegistros(): LiveData<List<RegistroEmergenciaEntity>>
    // Obtiene todos los registros de emergencia de la base de datos.
    // También retorna un LiveData para observar los cambios en tiempo real.



    @Query("DELETE FROM registros_emergencia WHERE creador = :usuario")
    suspend fun deleteRegistrosByUser(usuario: String)
    // Elimina todos los registros de emergencia que pertenecen a un usuario específico.



    @Query("DELETE FROM registros_emergencia")
    suspend fun deleteAllRegistros()
    // Elimina todos los registros de emergencia de la base de datos.
}