package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.delforjavier.emergenciaapp.data.AppDatabase
import com.delforjavier.emergenciaapp.data.RegistroEmergenciaEntity
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var registroId: Int = 0
    private var creadorOriginal: String = "" // Nuevo campo para guardar el creador original

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        database = AppDatabase.getDatabase(this)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cargar datos de emergencia"

        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val domicilio = findViewById<EditText>(R.id.etDomicilio)
        val adultos = findViewById<EditText>(R.id.etAdultos)
        val ninos = findViewById<EditText>(R.id.etNinos)
        val mayores = findViewById<EditText>(R.id.etMayores)
        val observaciones = findViewById<EditText>(R.id.etObservaciones)
        val switchTratamiento = findViewById<Switch>(R.id.switchTratamiento)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        val registroEditar = intent.getParcelableExtra<RegistroEmergencia>("registro_editar")
        registroId = registroEditar?.id ?: 0

        if (registroEditar != null) {
            nombre.setText(registroEditar.nombre)
            apellido.setText(registroEditar.apellido)
            domicilio.setText(registroEditar.domicilio)
            adultos.setText(registroEditar.cantidadAdultos.toString())
            mayores.setText(registroEditar.cantidadMayores.toString())
            ninos.setText(registroEditar.cantidadNinos.toString())
            observaciones.setText(registroEditar.observaciones)
            switchTratamiento.isChecked = registroEditar.tratamientoMedico
            btnGuardar.text = "Actualizar datos"

            // Guardar el creador original del registro
            creadorOriginal = registroEditar.creador
        }

        btnGuardar.setOnClickListener {
            val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
            val usuarioActual = prefs.getString("nombre", "desconocido") ?: "desconocido"
            val esOperador = prefs.getBoolean("es_operador", false)

            // Determinar el creador a guardar
            val creadorFinal = if (registroId != 0 && !esOperador) {
                // Si estamos editando y el usuario no es operador, usar el usuario actual
                usuarioActual
            } else if (registroId != 0 && esOperador && creadorOriginal.isNotEmpty()) {
                // Si es operador editando, mantener el creador original
                creadorOriginal
            } else {
                // Para nuevos registros, usar el usuario actual
                usuarioActual
            }

            val registro = RegistroEmergenciaEntity(
                id = registroId,
                nombre = nombre.text.toString(),
                apellido = apellido.text.toString(),
                domicilio = domicilio.text.toString(),
                cantidadAdultos = adultos.text.toString().toIntOrNull() ?: 0,
                cantidadMayores = mayores.text.toString().toIntOrNull() ?: 0,
                cantidadNinos = ninos.text.toString().toIntOrNull() ?: 0,
                observaciones = observaciones.text.toString(),
                tratamientoMedico = switchTratamiento.isChecked,
                creador = creadorFinal // Usamos el creador determinado
            )

            lifecycleScope.launch {
                try {
                    if (registroId != 0) {
                        database.registroEmergenciaDao().updateRegistro(registro)
                        runOnUiThread {
                            Toast.makeText(
                                this@RegistroActivity,
                                "Datos actualizados correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        database.registroEmergenciaDao().insertRegistro(registro)
                        runOnUiThread {
                            Toast.makeText(
                                this@RegistroActivity,
                                "Datos guardados correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    // Redirigir a la lista de datos
                    val intent = Intent(this@RegistroActivity, DatosIngresadosActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@RegistroActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}