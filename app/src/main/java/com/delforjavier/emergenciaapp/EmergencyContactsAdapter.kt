// EmergencyContactsAdapter.kt
// Adaptador personalizado para mostrar una lista de contactos de emergencia en un RecyclerView.

package com.delforjavier.emergenciaapp
// Define el paquete donde se encuentra este archivo.

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// Importa clases necesarias para trabajar con vistas y con RecyclerView.



class EmergencyContactsAdapter(private val contacts: List<EmergencyContact>) :
    RecyclerView.Adapter<EmergencyContactsAdapter.ViewHolder>() {
    // Adaptador que toma una lista de EmergencyContact y la muestra en un RecyclerView.
    // Se le pasa una lista de contactos por parámetro.

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Clase interna que representa cada ítem (fila) del RecyclerView.
        // Contiene las referencias a los elementos visuales del layout item_emergency_contact.xml.


        val icon: ImageView = view.findViewById(R.id.ivIcon)
        // Imagen que representa el ícono del contacto (por ejemplo, policía, bomberos).


        val name: TextView = view.findViewById(R.id.tvName)
        // Texto que muestra el nombre del contacto.


        val phone: TextView = view.findViewById(R.id.tvPhone)
        // Texto que muestra el número de teléfono del contacto.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Metodo que se llama cuando RecyclerView necesita crear una nueva fila.
        // Infla el layout XML (item_emergency_contact.xml) y crea un ViewHolder con él.

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Metodo que se llama para mostrar los datos en una fila específica.
        // Obtiene el contacto correspondiente a la posición y actualiza los textos e íconos.

        val contact = contacts[position]
        holder.icon.setImageResource(contact.iconResId) // Asigna el ícono al ImageView
        holder.name.text = contact.name // Asigna el nombre al TextView
        holder.phone.text = contact.phone  // Asigna el teléfono al TextView
    }

    override fun getItemCount() = contacts.size
    // Devuelve la cantidad total de elementos en la lista (cuántas filas debe mostrar el RecyclerView).
}