package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val administradoresValidos = listOf("12345678", "87654321") // DNIs válidos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val nombreInput = findViewById<EditText>(R.id.etNombreLogin)
        val dniInput = findViewById<EditText>(R.id.etDniLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val nombre = nombreInput.text.toString().trim()
            val dni = dniInput.text.toString().trim()

            if (nombre.isEmpty() || dni.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dni in administradoresValidos) {
                // Guardar en SharedPreferences si querés mostrarlo después
                val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
                prefs.edit()
                    .putString("nombre", nombre)
                    .putString("dni", dni)
                    .apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
