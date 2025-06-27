package com.delforjavier.emergenciaapp
// Paquete donde se encuentra la actividad, en este caso el principal de la app.

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
// Importaciones necesarias para funcionamiento con Room, UI, corrutinas, LiveData, etc.


class DatosIngresadosActivity : AppCompatActivity() {
    // Esta actividad muestra todos los registros de emergencia guardados por el usuario o por todos si es operador.


    private lateinit var containerLayout: LinearLayout // Layout donde se van a agregar dinámicamente las vistas de los registros
    private lateinit var database: AppDatabase  // Instancia de la base de datos
    private lateinit var repository: EmergenciaRepository // Repositorio que maneja los datos
    private var esOperador: Boolean = false // Define si el usuario tiene rol de operador
    private var usuarioActual: String = ""   // Guarda el nombre de usuario actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_ingresados) // Carga el layout de la pantalla

        // Inicializa la base de datos y el repositorio
        database = AppDatabase.getDatabase(this)
        repository = EmergenciaRepository(database.registroEmergenciaDao())

        // Configura el toolbar con botón de "atrás" y título
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ver Datos Cargados"

        // Referencias a elementos del layout
        containerLayout = findViewById(R.id.containerLayout)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        // Obtiene el nombre de usuario y si es operador desde SharedPreferences
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        usuarioActual = prefs.getString("nombre", "") ?: ""
        esOperador = prefs.getBoolean("es_operador", false)

        // Carga los datos observando cambios
        setupObservers()

        // Botón para eliminar registros (todos si es operador, propios si es usuario común)
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
        // Observa los registros correspondientes al usuario (todos si es operador)
        val registrosLiveData = repository.obtenerRegistrosParaUsuario(usuarioActual, esOperador)

        registrosLiveData.observe(this, Observer { listaRegistros ->
            containerLayout.removeAllViews() // Limpia la vista anterior

            if (listaRegistros.isNotEmpty()) {
                // Si hay registros, crea una vista para cada uno
                listaRegistros.forEach { registro ->
                    val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                    val txtResumen = registroView.findViewById<TextView>(R.id.txtResumenDatos)
                    val btnEditar = registroView.findViewById<Button>(R.id.btnEditar)
                    val btnEliminar = registroView.findViewById<Button>(R.id.btnEliminar)


                    // Arma un resumen con los datos del registro
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


                    // Botón para editar el registro (pasa el objeto por intent)
                    btnEditar.setOnClickListener {
                        val intent = Intent(this@DatosIngresadosActivity, RegistroActivity::class.java).apply {
                            putExtra("registro_editar", registro)
                        }
                        startActivity(intent)
                    }


                    // Botón para eliminar el registro con confirmación
                    btnEliminar.setOnClickListener {
                        // Mostrar diálogo de confirmación
                        android.app.AlertDialog.Builder(this@DatosIngresadosActivity)
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                            .setPositiveButton("Eliminar") { dialog, which ->
                                lifecycleScope.launch {
                                    try {
                                        // Convierte el modelo de dominio a entidad para eliminarlo
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

                    // Agrega la vista creada al contenedor principal
                    containerLayout.addView(registroView)
                }
            } else {
                // Si no hay registros, muestra un mensaje indicando que no hay datos
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
        // Vuelve a observar los datos al volver a esta pantalla (por ejemplo, después de editar)
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Maneja el botón de "volver" en el toolbar
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}