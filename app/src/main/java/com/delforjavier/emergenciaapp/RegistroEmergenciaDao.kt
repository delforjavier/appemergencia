package com.delforjavier.emergenciaapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroEmergenciaDao {

    @Insert
    suspend fun insertRegistro(registro: RegistroEmergenciaEntity): Long

    @Update
    suspend fun updateRegistro(registro: RegistroEmergenciaEntity)

    @Delete
    suspend fun deleteRegistro(registro: RegistroEmergenciaEntity)

    // ✅ Devuelve un Flow en vez de un List y no usa suspend
    @Query("SELECT * FROM registros_emergencia WHERE creador = :usuario")
    fun getRegistrosByUser(usuario: String): Flow<List<RegistroEmergenciaEntity>>

    // ✅ Devuelve un Flow en vez de un List y no usa suspend
    @Query("SELECT * FROM registros_emergencia")
    fun getAllRegistros(): Flow<List<RegistroEmergenciaEntity>>

    @Query("DELETE FROM registros_emergencia WHERE creador = :usuario")
    suspend fun deleteRegistrosByUser(usuario: String)

    @Query("DELETE FROM registros_emergencia")
    suspend fun deleteAllRegistros()
}
