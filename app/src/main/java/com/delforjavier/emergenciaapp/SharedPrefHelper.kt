package com.delforjavier.emergenciaapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefHelper(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("registro_pref", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun guardarRegistro(nuevoRegistro: RegistroEmergencia) {
        val listaExistente = obtenerListaRegistros().toMutableList()
        listaExistente.add(nuevoRegistro)

        val json = gson.toJson(listaExistente)
        sharedPreferences.edit().putString("registro_lista", json).apply()
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
}
