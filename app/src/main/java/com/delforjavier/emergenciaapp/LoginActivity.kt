package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.delforjavier.emergenciaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etNombreLogin.text.toString().trim()
            val password = binding.etDniLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar credenciales
            val prefs = getSharedPreferences("user_credentials", MODE_PRIVATE)
            val savedPassword = prefs.getString("password_$username", null)

            if (password == savedPassword) {
                // Guardar en SharedPreferences para mantener la sesión
                getSharedPreferences("usuario_login", MODE_PRIVATE).edit()
                    .putString("nombre", username)
                    .putString("dni", password)
                    .apply()

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
}