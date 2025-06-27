package com.delforjavier.emergenciaapp.data
// Define el paquete en el que se encuentra esta clase, en este caso dentro de la carpeta 'data'.


import androidx.room.Entity
import androidx.room.PrimaryKey
// Importa las anotaciones necesarias para definir esta clase como una entidad de Room.


@Entity(tableName = "users")
// Indica que esta clase representa una entidad de la base de datos Room.
// Se almacenará en una tabla llamada "users".


data class UserEntity(
    @PrimaryKey val username: String,
    // Define la clave primaria de la tabla, en este caso el nombre de usuario (username).
    // No se autogenera: cada usuario debe tener un username único.


    val password: String,
    // Contraseña del usuario. Se guarda en texto plano (aunque lo ideal en producción sería encriptarla).


    val isOperator: Boolean = false
    // Indica si el usuario tiene permisos de operador.
    // Los operadores pueden ver todos los registros de emergencia.
    // Por defecto es 'false', lo que significa que es un usuario común.
)