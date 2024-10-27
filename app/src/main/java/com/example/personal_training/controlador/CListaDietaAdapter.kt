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
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.MDieta
import com.google.android.material.snackbar.Snackbar

class CListaDietaAdapter (
    private val listaDietas: MutableList<Dieta>,
    private val mdieta: MDieta
) : RecyclerView.Adapter<CListaDietaAdapter.DietaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_dieta, parent, false)
        return DietaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietaViewHolder, position: Int) {
        val dieta = listaDietas[position]
        holder.tituloDietaTextView.text = dieta.titulo
        holder.descripcionDietaTextView.text = dieta.descripcion

        holder.btnEditar.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("dietaId", dieta.id!!)
            }
            holder.itemView.findNavController().navigate(R.id.action_nav_dietas_to_editarDietaFragment, bundle)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Dieta")
                .setMessage("¿Estás seguro de que deseas eliminar la dieta '${dieta.titulo}'?")
                .setPositiveButton("Sí") { _, _ ->
                    val resultado = mdieta.eliminarDieta(dieta.id!!)
                    if (resultado > 0) {
                        listaDietas.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                        Snackbar.make(it, "Dieta eliminada", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(it, "Error al eliminar la dieta", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount() = listaDietas.size

//    //no comentado
//    fun actualizarLista(nuevasDietas: List<Dieta>) {
//        listaDietas.clear() // Limpiamos la lista actual
//        listaDietas.addAll(nuevasDietas) // Añadimos las nuevas dietas
//        notifyDataSetChanged() // Notificamos al adaptador que los datos cambiaron
//    }
//
//    //no comentado
//    fun actualizarVista( dietaAdapter: CListaDietaAdapter) {
//        val dietasActualizadas = mdieta.obtenerDietas().toMutableList()
//        dietaAdapter.actualizarLista(dietasActualizadas)
//    }

    inner class DietaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloDietaTextView: TextView = itemView.findViewById(R.id.titulo_dieta)
        val descripcionDietaTextView: TextView = itemView.findViewById(R.id.descripcion_dieta)
        val btnEditar: Button = itemView.findViewById(R.id.btn_editar_dieta)
        val btnEliminar: Button = itemView.findViewById(R.id.btn_eliminar_dieta)
    }

}
