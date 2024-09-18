package com.example.personal_training.controlador

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.MCliente
import com.google.android.material.snackbar.Snackbar

class CListaClienteAdapter(
    private val listaClientes: MutableList<Cliente>,
    private val mcliente: MCliente
) : RecyclerView.Adapter<CListaClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.nombreTextView.text = cliente.nombre
        holder.telefonoTextView.text = cliente.telefono
        holder.correoTextView.text = cliente.correo

        holder.btnEditar.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("clienteId", cliente.id!!)
            }
            holder.itemView.findNavController().navigate(R.id.action_nav_clientes_to_editarClienteFragment, bundle)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estás seguro de que deseas eliminar a ${cliente.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    val resultado = mcliente.eliminarCliente(cliente.id!!)
                    if (resultado > 0) {
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
