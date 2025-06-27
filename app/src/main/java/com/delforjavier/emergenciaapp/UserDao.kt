package com.delforjavier.emergenciaapp.data
// Define el paquete donde se encuentra este archivo, en este caso dentro de la carpeta 'data'.


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
// Importa las anotaciones necesarias de Room para declarar un DAO y realizar operaciones en la base de datos.




@Dao
interface UserDao {
    // Marca esta interfaz como un DAO (Data Access Object), que sirve para acceder y manipular datos en la tabla "users".


    @Insert
    suspend fun insertUser(user: UserEntity)
    // Inserta un nuevo usuario en la base de datos.
    // La función es 'suspend' porque debe ejecutarse en una corrutina (fuera del hilo principal).



    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): UserEntity?
    // Busca y devuelve un usuario por su nombre de usuario.
    // Si no lo encuentra, devuelve null.
    // El uso de ':username' permite pasar ese valor como parámetro desde Kotlin.



    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
    // Elimina todos los registros de usuarios de la base de datos.

}