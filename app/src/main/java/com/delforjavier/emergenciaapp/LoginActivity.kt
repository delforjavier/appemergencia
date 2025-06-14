package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.delforjavier.emergenciaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val operadores = mapOf(
        "Maximo" to "max123",
        "Bernardo" to "ber123",
        "Pablo" to "pab123",
        "Javier" to "jav123",
        "Juan Pablo" to "juan123"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsuarioLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar si es un operador
            if (operadores.containsKey(username)) {
                if (password == operadores[username]) {
                    guardarSesion(username, password, true)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            // Verificar credenciales de usuario común
            val prefs = getSharedPreferences("user_credentials", MODE_PRIVATE)
            val savedPassword = prefs.getString("password_$username", null)

            if (password == savedPassword) {
                guardarSesion(username, password, false)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun guardarSesion(username: String, password: String, esOperador: Boolean) {
        getSharedPreferences("usuario_login", MODE_PRIVATE).edit()
            .putString("nombre", username)
            .putString("contraseña", password)
            .putBoolean("es_operador", esOperador)
            .apply()
    }
}