package com.delforjavier.emergenciaapp
// Define el paquete donde se encuentra este archivo, en este caso el paquete principal de la app.


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
// Importa lo necesario para hacer que el objeto sea "parcelable", lo cual permite pasarlo entre pantallas (Activities o Fragments).


@Parcelize
data class RegistroEmergencia(
    val id: Int = 0,
    // ID del registro. Es 0 por defecto, y normalmente lo asigna la base de datos al insertar.

    val nombre: String,
    // Nombre de la persona en situación de emergencia.

    val apellido: String,
    // Apellido de la persona.

    val domicilio: String,
    // Dirección donde ocurrió la emergencia.

    val cantidadAdultos: Int,
    // Cantidad de adultos en el domicilio.

    val cantidadMayores: Int,
    // Cantidad de adultos mayores (personas mayores de edad avanzada).

    val cantidadNinos: Int,
    // Cantidad de niños presentes.

    val observaciones: String,
    // Texto libre para agregar observaciones adicionales sobre la emergencia.

    val tratamientoMedico: Boolean,
    // Indica si alguna persona en el domicilio requiere tratamiento médico.

    val creador: String = ""
    // Nombre de usuario que creó el registro. Por defecto está vacío.

) : Parcelable
// Hace que esta clase pueda ser pasada entre Activities y Fragments mediante Intents o Bundles.
