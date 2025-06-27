package com.delforjavier.emergenciaapp.data
// Define el paquete al que pertenece esta clase, en este caso la carpeta 'data' del proyecto.


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable  // Importa Serializable para poder pasar esta clase entre Activities o Fragments con Intents


// Indica que esta clase representa una entidad (tabla) en la base de datos Room.
@Entity(tableName = "registros_emergencia")
data class RegistroEmergenciaEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Define la clave primaria de la tabla.
    // 'autoGenerate = true' hace que Room genere automáticamente el ID al insertar un nuevo registro.

    val nombre: String,
    // Nombre de la persona registrada en la emergencia.

    val apellido: String,
    // Apellido de la persona.

    val domicilio: String,
    // Dirección del domicilio donde ocurrió la emergencia.

    val cantidadAdultos: Int,
    // Cantidad de adultos presentes en el domicilio.

    val cantidadMayores: Int,
    // Cantidad de adultos mayores (probablemente personas de edad avanzada).

    val cantidadNinos: Int,
    // Cantidad de niños presentes.

    val observaciones: String,
    // Observaciones adicionales del caso (por ejemplo, si hay mascotas, movilidad reducida, etc.).

    val tratamientoMedico: Boolean,
    // Indica si alguna de las personas requiere tratamiento médico.

    val creador: String
    // Nombre de usuario que creó este registro (sirve para filtrar registros por usuario).

) : Serializable
// Implementa Serializable para que los objetos de esta clase puedan ser enviados entre Activities mediante Intents.
