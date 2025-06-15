package com.delforjavier.emergenciaapp

import android.content.Intent  // Importación añadida
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DatosIngresadosActivity : AppCompatActivity() {

    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var containerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_ingresados)

        // Configurar toolbar como ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ver Datos Cargados"

        sharedPrefHelper = SharedPrefHelper(this)
        containerLayout = findViewById(R.id.containerLayout)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        // Limpiar el contenedor antes de agregar nuevos elementos
        containerLayout.removeAllViews()

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
            listaRegistros.forEachIndexed { index, registro ->
                val registroView = layoutInflater.inflate(R.layout.item_registro, null)

                val txtResumen = registroView.findViewById<TextView>(R.id.txtResumenDatos)
                val btnEditar = registroView.findViewById<Button>(R.id.btnEditar)

                val resumen = """
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

                txtResumen.text = resumen

                btnEditar.setOnClickListener {
                    val intent = Intent(this, RegistroActivity::class.java).apply {
                        putExtra("registro_editar", registro)  // Especifica el tipo si es necesario
                        putExtra("indice_registro", index)
                    }
                    startActivity(intent)
                }

                containerLayout.addView(registroView)
            }
        } else {
            val emptyView = TextView(this).apply {
                text = "No hay datos disponibles."
                textSize = 18f
                setPadding(0, 16, 0, 16)
            }
            containerLayout.addView(emptyView)
        }

        btnLimpiar.setOnClickListener {
            if (esOperador) {
                sharedPrefHelper.limpiarRegistros()
            } else {
                val todosRegistros = sharedPrefHelper.obtenerListaRegistros().toMutableList()
                val misRegistros = sharedPrefHelper.obtenerRegistrosPorUsuario(usuarioActual ?: "")
                todosRegistros.removeAll(misRegistros)
                val json = sharedPrefHelper.gson.toJson(todosRegistros)
                sharedPrefHelper.sharedPreferences.edit()
                    .putString("registro_lista", json)
                    .apply()
            }
            Toast.makeText(this, "Datos eliminados", Toast.LENGTH_SHORT).show()
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
