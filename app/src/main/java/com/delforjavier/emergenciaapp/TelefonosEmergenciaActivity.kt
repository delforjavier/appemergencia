// TelefonosEmergenciaActivity.kt
package com.delforjavier.emergenciaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TelefonosEmergenciaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telefonos_emergencia)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Teléfonos de Emergencia"

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvEmergencyContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear lista de contactos de emergencia
        val emergencyContacts = listOf(
            EmergencyContact("Bomberos", "100", R.drawable.ic_bombero),
            EmergencyContact("Policía", "911", R.drawable.ic_policia),
            EmergencyContact("SAME", "107", R.drawable.ic_same),
            EmergencyContact("Defensa Civil", "103", R.drawable.ic_defensa)
        )

        // Configurar adaptador
        recyclerView.adapter = EmergencyContactsAdapter(emergencyContacts)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}