// EmergencyContact.kt
// Este archivo define una clase de datos (data class) que representa un contacto de emergencia.

package com.delforjavier.emergenciaapp
// Define el paquete donde se encuentra esta clase, en este caso el principal de la app.


data class EmergencyContact(
    val name: String,
    // Nombre del contacto de emergencia.

    val phone: String,
    // Número de teléfono del contacto de emergencia.

    val iconResId: Int
    // ID del recurso de ícono (imagen) que se mostrará en la interfaz junto al contacto.
)
