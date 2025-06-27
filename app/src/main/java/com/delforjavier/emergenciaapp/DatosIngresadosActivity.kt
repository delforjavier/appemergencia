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
import androidx.lifecycle.Observer
import com.delforjavier.emergenciaapp.data.AppDatabase
import com.delforjavier.emergenciaapp.data.EmergenciaRepository
import kotlinx.coroutines.launch
import com.delforjavier.emergenciaapp.data.RegistroEmergenciaEntity

class DatosIngresadosActivity : AppCompatActivity() {

    private lateinit var containerLayout: LinearLayout
    private lateinit var database: AppDatabase
    private lateinit var repository: EmergenciaRepository
    private var esOperador: Boolean = false
    private var usuarioActual: String = ""

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
        usuarioActual = prefs.getString("nombre", "") ?: ""
        esOperador = prefs.getBoolean("es_operador", false)

        setupObservers()

        btnLimpiar.setOnClickListener {
            lifecycleScope.launch {
                try {
                    if (esOperador) {
                        repository.eliminarTodosRegistros()
                    } else {
                        repository.eliminarRegistrosPorUsuario(usuarioActual)
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

    private fun setupObservers() {
        val registrosLiveData = repository.obtenerRegistrosParaUsuario(usuarioActual, esOperador)

        registrosLiveData.observe(this, Observer { listaRegistros ->
            containerLayout.removeAllViews()

            if (listaRegistros.isNotEmpty()) {
                listaRegistros.forEach { registro ->
                    val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                    val txtResumen = registroView.findViewById<TextView>(R.id.txtResumenDatos)
                    val btnEditar = registroView.findViewById<Button>(R.id.btnEditar)
                    val btnEliminar = registroView.findViewById<Button>(R.id.btnEliminar)

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
                        }
                        startActivity(intent)
                    }

                    btnEliminar.setOnClickListener {
                        // Mostrar diálogo de confirmación
                        android.app.AlertDialog.Builder(this@DatosIngresadosActivity)
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                            .setPositiveButton("Eliminar") { dialog, which ->
                                lifecycleScope.launch {
                                    try {
                                        // Convertir a entidad para eliminar
                                        val registroEntity = RegistroEmergenciaEntity(
                                            id = registro.id,
                                            nombre = registro.nombre,
                                            apellido = registro.apellido,
                                            domicilio = registro.domicilio,
                                            cantidadAdultos = registro.cantidadAdultos,
                                            cantidadMayores = registro.cantidadMayores,
                                            cantidadNinos = registro.cantidadNinos,
                                            observaciones = registro.observaciones,
                                            tratamientoMedico = registro.tratamientoMedico,
                                            creador = registro.creador
                                        )
                                        repository.eliminarRegistro(registroEntity)
                                        Toast.makeText(
                                            this@DatosIngresadosActivity,
                                            "Registro eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            this@DatosIngresadosActivity,
                                            "Error al eliminar: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            .setNegativeButton("Cancelar", null)
                            .show()
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
        })
    }

    override fun onResume() {
        super.onResume()
        // Forzar actualización al volver a la actividad
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}