package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.delforjavier.emergenciaapp.data.AppDatabase
import com.delforjavier.emergenciaapp.data.UserEntity
import com.delforjavier.emergenciaapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        // Insertar operadores por primera vez
        lifecycleScope.launch {
            val operadores = listOf(
                UserEntity("Maximo", "max123", true),
                UserEntity("Bernardo", "ber123", true),
                UserEntity("Pablo", "pab123", true),
                UserEntity("Javier", "jav123", true),
                UserEntity("Juan Pablo", "juan123", true)
            )

            operadores.forEach { operador ->
                if (database.userDao().getUser(operador.username) == null) {
                    database.userDao().insertUser(operador)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsuarioLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = database.userDao().getUser(username)

                if (user != null && user.password == password) {
                    guardarSesion(username, password, user.isOperator)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
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