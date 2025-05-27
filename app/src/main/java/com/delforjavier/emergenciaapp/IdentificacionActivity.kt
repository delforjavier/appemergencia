package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IdentificacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identificacion)

        val etNombre = findViewById<EditText>(R.id.etNombreUsuario)
        val etDNI = findViewById<EditText>(R.id.etDNI)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        btnIngresar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val dni = etDNI.text.toString()

            if (nombre.isNotBlank() && dni.isNotBlank()) {
                // Aquí podés guardar los datos si querés usarlos después
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
