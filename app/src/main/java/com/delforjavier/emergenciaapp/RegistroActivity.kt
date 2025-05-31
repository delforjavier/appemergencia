package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Flecha para volver atrás
        supportActionBar?.title = "Cargar datos de emergencia "

        sharedPrefHelper = SharedPrefHelper(this)

        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val domicilio = findViewById<EditText>(R.id.etDomicilio)
        val adultos = findViewById<EditText>(R.id.etAdultos)
        val ninos = findViewById<EditText>(R.id.etNinos)
        val mayores = findViewById<EditText>(R.id.etMayores)

        val switchTratamiento = findViewById<Switch>(R.id.switchTratamiento)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val registro = RegistroEmergencia(
                nombre = nombre.text.toString(),
                apellido = apellido.text.toString(),
                domicilio = domicilio.text.toString(),
                cantidadAdultos = adultos.text.toString().toIntOrNull() ?: 0,
                cantidadMayores = mayores.text.toString().toIntOrNull() ?: 0,
                cantidadNinos = ninos.text.toString().toIntOrNull() ?: 0,
                tratamientoMedico = switchTratamiento.isChecked
            )

            sharedPrefHelper.guardarRegistro(registro)

            Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()

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
