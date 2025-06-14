package com.delforjavier.emergenciaapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DatosIngresadosActivity : AppCompatActivity() {

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_ingresados)

        // Configurar toolbar como ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ver Datos Cargados"

        sharedPrefHelper = SharedPrefHelper(this)
        val txtResumen = findViewById<TextView>(R.id.txtResumenDatos)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        // Obtener información del usuario actual
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        val usuarioActual = prefs.getString("nombre", "")
        val esOperador = prefs.getBoolean("es_operador", false)

        // Obtener registros según el tipo de usuario
        val listaRegistros = if (esOperador) {
            sharedPrefHelper.obtenerListaRegistros() // Operadores ven todos los registros
        } else {
            sharedPrefHelper.obtenerRegistrosPorUsuario(usuarioActual ?: "") // Usuarios comunes ven solo sus registros
        }

        if (listaRegistros.isNotEmpty()) {
            val resumen = listaRegistros.joinToString("\n\n") { registro ->
                """
                Nombre: ${registro.nombre}
                Apellido: ${registro.apellido}
                Domicilio: ${registro.domicilio}
                Adultos: ${registro.cantidadAdultos}
                Mayores: ${registro.cantidadMayores}
                Niños: ${registro.cantidadNinos}
                Observaciones: ${registro.observaciones}
                Tratamiento Médico: ${if (registro.tratamientoMedico) "Sí" else "No"}
                ${if (esOperador) "Creado por: ${registro.creador}" else ""}
                """.trimIndent()
            }
            txtResumen.text = resumen
        } else {
            txtResumen.text = "No hay datos disponibles."
        }

        btnLimpiar.setOnClickListener {
            if (esOperador) {
                // Operadores pueden limpiar todos los registros
                sharedPrefHelper.limpiarRegistros()
            } else {
                // Usuarios comunes solo limpian sus propios registros
                val todosRegistros = sharedPrefHelper.obtenerListaRegistros().toMutableList()
                val misRegistros = sharedPrefHelper.obtenerRegistrosPorUsuario(usuarioActual ?: "")

                // Eliminar solo los registros del usuario actual
                todosRegistros.removeAll(misRegistros)

                // Guardar la lista actualizada
                val json = sharedPrefHelper.gson.toJson(todosRegistros)
                sharedPrefHelper.sharedPreferences.edit()
                    .putString("registro_lista", json)
                    .apply()
            }
            Toast.makeText(this, "Datos eliminados", Toast.LENGTH_SHORT).show()
            txtResumen.text = "No hay datos disponibles."
            finish()
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
