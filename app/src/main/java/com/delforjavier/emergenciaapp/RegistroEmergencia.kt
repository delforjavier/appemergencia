package com.delforjavier.emergenciaapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistroEmergencia(
    val nombre: String,
    val apellido: String,
    val domicilio: String,
    val cantidadAdultos: Int,
    val cantidadMayores: Int,
    val cantidadNinos: Int,
    val observaciones: String,
    val tratamientoMedico: Boolean,
    val creador: String = ""
) : Parcelable