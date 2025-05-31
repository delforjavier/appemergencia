package com.delforjavier.emergenciaapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar como ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        // Botón hamburguesa
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Mostrar nombre del usuario en el header
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "Usuario")
        val headerView = navView.getHeaderView(0)
        val txtNombreUsuario = headerView.findViewById<TextView>(R.id.txtNombreUsuario)
        txtNombreUsuario.text = "Bienvenido/a, $nombre"

        // Click en ítems del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_registro -> {
                    startActivity(Intent(this, RegistroActivity::class.java))
                }
                R.id.nav_confirmacion -> {
                    startActivity(Intent(this, DatosIngresadosActivity::class.java))
                }
                R.id.nav_contactos -> {
                    startActivity(Intent(this, TelefonosEmergenciaActivity::class.java))
                }
                R.id.nav_logout -> {
                    cerrarSesion()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        prefs.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}

