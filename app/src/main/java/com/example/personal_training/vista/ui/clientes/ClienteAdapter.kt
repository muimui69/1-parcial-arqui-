package com.example.personal_training.vista.ui.clientes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.controlador.CCliente
import com.example.personal_training.modelo.Cliente
import com.google.android.material.snackbar.Snackbar

class ClienteAdapter(private val listaClientes: MutableList<Cliente>,
                     private val controladorCliente: CCliente) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

//    private lateinit var controladorCliente: CCliente

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

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estás seguro de que deseas eliminar a ${cliente.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    // Intentar eliminar el cliente de la base de datos
                    val resultado = controladorCliente.eliminarCliente(cliente.id!!)
                    if (resultado > 0) {
                        // Eliminar el cliente de la lista y notificar al adaptador
                        listaClientes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                        Snackbar.make(it, "Cliente eliminado", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(it, "Error al eliminar el cliente", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
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
