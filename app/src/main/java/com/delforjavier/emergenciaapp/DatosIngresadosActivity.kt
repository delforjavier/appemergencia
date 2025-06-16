package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.delforjavier.emergenciaapp.data.AppDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DatosIngresadosActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var containerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_ingresados)

        database = AppDatabase.getDatabase(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ver Datos Cargados"

        containerLayout = findViewById(R.id.containerLayout)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        val usuarioActual = prefs.getString("nombre", "")
        val esOperador = prefs.getBoolean("es_operador", false)

        lifecycleScope.launch {
            val flow = if (esOperador) {
                database.registroEmergenciaDao().getAllRegistros()
            } else {
                database.registroEmergenciaDao().getRegistrosByUser(usuarioActual ?: "")
            }

            flow.collect { listaRegistros ->
                runOnUiThread {
                    containerLayout.removeAllViews()

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
                                val intent = Intent(this@DatosIngresadosActivity, RegistroActivity::class.java).apply {
                                    putExtra("registro_editar", registro)
                                    putExtra("indice_registro", registro.id)
                                }
                                startActivity(intent)
                            }

                            containerLayout.addView(registroView)
                        }
                    } else {
                        val emptyView = TextView(this@DatosIngresadosActivity).apply {
                            text = "No hay datos disponibles."
                            textSize = 18f
                            setPadding(0, 16, 0, 16)
                        }
                        containerLayout.addView(emptyView)
                    }
                }
            }
        }

        btnLimpiar.setOnClickListener {
            lifecycleScope.launch {
                if (esOperador) {
                    database.registroEmergenciaDao().deleteAllRegistros()
                } else {
                    database.registroEmergenciaDao().deleteRegistrosByUser(usuarioActual ?: "")
                }

                runOnUiThread {
                    Toast.makeText(this@DatosIngresadosActivity, "Datos eliminados", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(intent)
                }
            }
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
