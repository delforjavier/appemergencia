package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.delforjavier.emergenciaapp.data.AppDatabase
import com.delforjavier.emergenciaapp.data.UserEntity
import com.delforjavier.emergenciaapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (database.userDao().getUser(username) != null) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                database.userDao().insertUser(UserEntity(username, password))
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // Nuevo botón Volver
        binding.btnVolver.setOnClickListener {
            // Volver a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Opcional: cierra esta actividad para que no quede en la pila
        }
    }
}