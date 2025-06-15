package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    private lateinit var sharedPrefHelper: SharedPrefHelper
    private var indiceEdicion: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cargar datos de emergencia"

        sharedPrefHelper = SharedPrefHelper(this)

        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val domicilio = findViewById<EditText>(R.id.etDomicilio)
        val adultos = findViewById<EditText>(R.id.etAdultos)
        val ninos = findViewById<EditText>(R.id.etNinos)
        val mayores = findViewById<EditText>(R.id.etMayores)
        val observaciones = findViewById<EditText>(R.id.etObservaciones)
        val switchTratamiento = findViewById<Switch>(R.id.switchTratamiento)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Verificar si estamos editando un registro existente
        val registroEditar = intent.getParcelableExtra<RegistroEmergencia>("registro_editar")
        indiceEdicion = intent.getIntExtra("indice_registro", -1)

        if (registroEditar != null) {
            // Llenar los campos con los datos del registro a editar
            nombre.setText(registroEditar.nombre)
            apellido.setText(registroEditar.apellido)
            domicilio.setText(registroEditar.domicilio)
            adultos.setText(registroEditar.cantidadAdultos.toString())
            mayores.setText(registroEditar.cantidadMayores.toString())
            ninos.setText(registroEditar.cantidadNinos.toString())
            observaciones.setText(registroEditar.observaciones)
            switchTratamiento.isChecked = registroEditar.tratamientoMedico
            btnGuardar.text = "Actualizar datos"
        }

        btnGuardar.setOnClickListener {
            // Obtener el usuario actual desde SharedPreferences
            val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
            val usuarioActual = prefs.getString("nombre", "desconocido") ?: "desconocido"

            val registro = RegistroEmergencia(
                nombre = nombre.text.toString(),
                apellido = apellido.text.toString(),
                domicilio = domicilio.text.toString(),
                cantidadAdultos = adultos.text.toString().toIntOrNull() ?: 0,
                cantidadMayores = mayores.text.toString().toIntOrNull() ?: 0,
                cantidadNinos = ninos.text.toString().toIntOrNull() ?: 0,
                observaciones = observaciones.text.toString(),
                tratamientoMedico = switchTratamiento.isChecked,
                creador = usuarioActual
            )

            if (indiceEdicion != -1) {
                // Actualizar registro existente
                sharedPrefHelper.actualizarRegistro(indiceEdicion, registro)
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            } else {
                // Guardar nuevo registro
                sharedPrefHelper.guardarRegistro(registro, usuarioActual)
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, DatosIngresadosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}