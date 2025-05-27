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
        val listaRegistros = sharedPrefHelper.obtenerListaRegistros()
        val txtResumen = findViewById<TextView>(R.id.txtResumenDatos)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        if (listaRegistros.isNotEmpty()) {
            val resumen = listaRegistros.joinToString("\n\n") { registro ->
                """
                Nombre: ${registro.nombre}
                Apellido: ${registro.apellido}
                Domicilio: ${registro.domicilio}
                Adultos: ${registro.cantidadAdultos}
                Mayores: ${registro.cantidadMayores}
                Niños: ${registro.cantidadNinos}
                Tratamiento Médico: ${if (registro.tratamientoMedico) "Sí" else "No"}
                """.trimIndent()
            }
            txtResumen.text = resumen
        } else {
            txtResumen.text = "No hay datos disponibles."
        }

        btnLimpiar.setOnClickListener {
            sharedPrefHelper.limpiarRegistros()
            Toast.makeText(this, "Datos eliminados", Toast.LENGTH_SHORT).show()
            txtResumen.text = "No hay datos disponibles."
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
