package com.delforjavier.emergenciaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable  // <- Import necesario

@Entity(tableName = "registros_emergencia")
data class RegistroEmergenciaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val domicilio: String,
    val cantidadAdultos: Int,
    val cantidadMayores: Int,
    val cantidadNinos: Int,
    val observaciones: String,
    val tratamientoMedico: Boolean,
    val creador: String
) : Serializable  // <- Esto es lo que te permite enviarlo por Intent
