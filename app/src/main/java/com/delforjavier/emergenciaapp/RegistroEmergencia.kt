package com.delforjavier.emergenciaapp

data class RegistroEmergencia(
    val nombre: String,
    val apellido: String,
    val domicilio: String,
    val cantidadAdultos: Int,
    val cantidadMayores: Int,
    val cantidadNinos: Int,
    val observaciones: String,
    val tratamientoMedico: Boolean
)
