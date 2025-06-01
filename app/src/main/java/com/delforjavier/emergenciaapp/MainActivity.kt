// Paquete principal de la app
package com.delforjavier.emergenciaapp

// Importación de clases necesarias
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

// Clase principal de la app, que hereda de AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Declaración de variables para el layout del drawer y el menú lateral
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    // Metodo que se ejecuta al iniciar la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el layout a utilizar
        setContentView(R.layout.activity_main)

        // Obtiene la toolbar del layout y la establece como la ActionBar de la actividad
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Asocia las vistas del Drawer y del menú lateral (NavigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        // Configura el botón "hamburguesa" que abre/cierra el menú lateral
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,// Texto para accesibilidad al abrir
            R.string.navigation_drawer_close // Texto para accesibilidad al cerrar
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState() // Sincroniza el estado del botón con el Drawer

        // Recupera el nombre del usuario desde las preferencias compartidas
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "Usuario")

        // Obtiene la vista del encabezado del menú lateral y le pone el nombre del usuario
        val headerView = navView.getHeaderView(0)
        val txtNombreUsuario = headerView.findViewById<TextView>(R.id.txtNombreUsuario)
        txtNombreUsuario.text = "Bienvenido/a, $nombre"

        // Maneja los clics en los ítems del menú lateral
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_registro -> {

                    // Abre la actividad de registro
                    startActivity(Intent(this, RegistroActivity::class.java))
                }
                R.id.nav_confirmacion -> {

                    // Abre la actividad que muestra los datos ingresados
                    startActivity(Intent(this, DatosIngresadosActivity::class.java))
                }
                R.id.nav_contactos -> {

                    // Abre la actividad con teléfonos de emergencia
                    startActivity(Intent(this, TelefonosEmergenciaActivity::class.java))
                }
                R.id.nav_logout -> {

                    // Cierra la sesión del usuario
                    cerrarSesion()
                }
            }

            // Cierra el menú lateral después de hacer clic
            drawerLayout.closeDrawers()
            true
        }
    }

    // Función para cerrar sesión y volver a la pantalla de login
    private fun cerrarSesion() {

        // Borra los datos guardados en SharedPreferences
        val prefs = getSharedPreferences("usuario_login", MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Crea un intent para volver a la pantalla de login y limpia el historial de actividades
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // Finaliza esta actividad
        finish()
    }

    // Si se presiona el botón "Atrás" y el menú está abierto, lo cierra en lugar de salir de la app
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}

