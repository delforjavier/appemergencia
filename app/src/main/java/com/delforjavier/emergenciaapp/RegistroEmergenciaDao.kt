package com.delforjavier.emergenciaapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RegistroEmergenciaDao {
    @Insert
    suspend fun insertRegistro(registro: RegistroEmergenciaEntity): Long

    @Update
    suspend fun updateRegistro(registro: RegistroEmergenciaEntity)

    @Delete
    suspend fun deleteRegistro(registro: RegistroEmergenciaEntity)

    @Query("SELECT * FROM registros_emergencia WHERE creador = :usuario")
    fun getRegistrosByUser(usuario: String): LiveData<List<RegistroEmergenciaEntity>>

    @Query("SELECT * FROM registros_emergencia")
    fun getAllRegistros(): LiveData<List<RegistroEmergenciaEntity>>

    @Query("DELETE FROM registros_emergencia WHERE creador = :usuario")
    suspend fun deleteRegistrosByUser(usuario: String)

    @Query("DELETE FROM registros_emergencia")
    suspend fun deleteAllRegistros()
}