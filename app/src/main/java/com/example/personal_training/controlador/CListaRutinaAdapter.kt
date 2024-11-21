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
import com.example.personal_training.controlador.command.EliminarRutinaCommand
import com.example.personal_training.controlador.command.RutinaInvoker
import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina
import com.google.android.material.snackbar.Snackbar

class CListaRutinaAdapter(
    private val listaRutinas: MutableList<Rutina>,
    private val mrutina: MRutina
) : RecyclerView.Adapter<CListaRutinaAdapter.RutinaViewHolder>() {

    private val invoker  = RutinaInvoker()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_rutina, parent, false)
        return RutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = listaRutinas[position]

        holder.nombreRutinaTextView.text = rutina.nombre
        holder.tipoRutinaTextView.text = rutina.tipo

        holder.btnEditar.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("rutinaId", rutina.id!!)
            }
            holder.itemView.findNavController().navigate(R.id.action_nav_rutinas_to_editarRutinaFragment, bundle)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Rutina")
                .setMessage("¿Estás seguro de que deseas eliminar la rutina '${rutina.nombre}'?")
                .setPositiveButton("Sí") { _, _ ->

//                    val resultado = mrutina.eliminarRutina(rutina.id!!)
//                    if (resultado > 0) {
//                        listaRutinas.removeAt(position)
//                        notifyItemRemoved(position)
//                        notifyItemRangeChanged(position, itemCount)
//                        Snackbar.make(it, "Rutina eliminada", Snackbar.LENGTH_SHORT).show()
//                    } else {
//                        Snackbar.make(it, "Error al eliminar la rutina", Snackbar.LENGTH_SHORT).show()
//                    }

                    //desiggn pattern command
                    val eliminarCommand = EliminarRutinaCommand(mrutina, rutina.id!!)
                    invoker.setCommand(eliminarCommand)
                    invoker.executeCommand()

                    listaRutinas.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                    //Snackbar.make(holder.itemView, "Rutina eliminada", Snackbar.LENGTH_SHORT).show()
                    // Mostrar un Snackbar con opción para deshacer
                    Snackbar.make(holder.itemView, "Rutina eliminada", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            invoker.undoCommand()
                            listaRutinas.add(position, rutina)
                            notifyItemInserted(position)
                            notifyItemRangeChanged(position, itemCount)
                            Snackbar.make(holder.itemView, "Eliminación deshecha", Snackbar.LENGTH_SHORT).show()
                        }
                        .show()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount() = listaRutinas.size

    inner class RutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreRutinaTextView: TextView = itemView.findViewById(R.id.nombre_rutina)
        val tipoRutinaTextView: TextView = itemView.findViewById(R.id.tipo_rutina)
        val btnEditar: Button = itemView.findViewById(R.id.btn_editar_rutina)
        val btnEliminar: Button = itemView.findViewById(R.id.btn_eliminar_rutina)
    }
}
