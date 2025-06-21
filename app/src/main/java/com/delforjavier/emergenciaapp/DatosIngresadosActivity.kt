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
import com.delforjavier.emergenciaapp.data.EmergenciaRepository
import kotlinx.coroutines.launch

class DatosIngresadosActivity : AppCompatActivity() {

    private lateinit var containerLayout: LinearLayout
    private lateinit var database: AppDatabase
    private lateinit var repository: EmergenciaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_ingresados)

        database = AppDatabase.getDatabase(this)
        repository = EmergenciaRepository(database.registroEmergenciaDao())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ver Datos Cargados"

        containerLayout = findViewById(R.id.containerLayout)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        val usuarioActual = prefs.getString("nombre", "")
        val esOperador = prefs.getBoolean("es_operador", false)

        // Observar los datos con LiveData
        val registrosLiveData = if (esOperador) {
            repository.obtenerTodosRegistros()
        } else {
            repository.obtenerRegistrosPorUsuario(usuarioActual ?: "")
        }

        registrosLiveData.observe(this) { listaRegistros ->
            containerLayout.removeAllViews()

            if (listaRegistros.isNotEmpty()) {
                listaRegistros.forEach { registro ->
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
                            putExtra("indice_registro", registro.id ?: 0)  // Usamos el operador elvis por seguridad
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

        btnLimpiar.setOnClickListener {
            lifecycleScope.launch { // Corregido: Usamos lifecycleScope para corrutinas
                try {
                    if (esOperador) {
                        repository.eliminarTodosRegistros()
                    } else {
                        usuarioActual?.let { user ->
                            repository.eliminarRegistrosPorUsuario(user)
                        }
                    }
                    Toast.makeText(
                        this@DatosIngresadosActivity,
                        "Datos eliminados",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@DatosIngresadosActivity,
                        "Error al eliminar datos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
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