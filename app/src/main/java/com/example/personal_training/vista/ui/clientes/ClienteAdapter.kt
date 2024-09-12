package com.example.personal_training.vista.ui.clientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.modelo.Cliente
import com.google.android.material.snackbar.Snackbar

class ClienteAdapter(private val listaClientes: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }


    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.nombreTextView.text = cliente.nombre
        holder.telefonoTextView.text = cliente.telefono
        holder.correoTextView.text = cliente.correo

        // Configurar botón editar
        holder.btnEditar.setOnClickListener {
            // Navegar a la pantalla de editar cliente o mostrar un diálogo
            Snackbar.make(it, "Editar cliente: ${cliente.nombre}", Snackbar.LENGTH_LONG).show()
            // Aquí puedes implementar la lógica para editar
        }

        // Configurar botón eliminar
        holder.btnEliminar.setOnClickListener {
            // Eliminar cliente y actualizar la lista
            Snackbar.make(it, "Eliminar cliente: ${cliente.nombre}", Snackbar.LENGTH_LONG).show()
            // Implementar lógica de eliminación
        }

    }

    override fun getItemCount() = listaClientes.size

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombre_cliente)
        val telefonoTextView: TextView = itemView.findViewById(R.id.telefono_cliente)
        val correoTextView: TextView = itemView.findViewById(R.id.correo_cliente)
        val btnEditar: Button = itemView.findViewById(R.id.btn_editar_cliente)
        val btnEliminar: Button = itemView.findViewById(R.id.btn_eliminar_cliente)
    }

}
