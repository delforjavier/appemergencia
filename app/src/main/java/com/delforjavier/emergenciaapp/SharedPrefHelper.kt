package com.delforjavier.emergenciaapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefHelper(private val context: Context) {

    val sharedPreferences = context.getSharedPreferences("registro_pref", Context.MODE_PRIVATE)
    val gson = Gson()

    fun guardarRegistro(nuevoRegistro: RegistroEmergencia, creador: String) {
        val listaExistente = obtenerListaRegistros().toMutableList()
        val registroConCreador = nuevoRegistro.copy(creador = creador)
        listaExistente.add(registroConCreador)
        guardarListaRegistros(listaExistente)
    }

    fun actualizarRegistro(indice: Int, registroActualizado: RegistroEmergencia) {
        val listaExistente = obtenerListaRegistros().toMutableList()
        if (indice in 0 until listaExistente.size) {
            // Mantener el creador original del registro
            val creadorOriginal = listaExistente[indice].creador
            listaExistente[indice] = registroActualizado.copy(creador = creadorOriginal)
            guardarListaRegistros(listaExistente)
        }
    }

    fun obtenerRegistrosPorUsuario(usuario: String): List<RegistroEmergencia> {
        val todos = obtenerListaRegistros()
        return todos.filter { it.creador == usuario }
    }

    fun obtenerListaRegistros(): List<RegistroEmergencia> {
        val json = sharedPreferences.getString("registro_lista", null)
        if (json.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<RegistroEmergencia>>() {}.type
        return gson.fromJson(json, type)
    }

    fun limpiarRegistros() {
        sharedPreferences.edit().remove("registro_lista").apply()
    }

    private fun guardarListaRegistros(lista: List<RegistroEmergencia>) {
        val json = gson.toJson(lista)
        sharedPreferences.edit().putString("registro_lista", json).apply()
    }
}