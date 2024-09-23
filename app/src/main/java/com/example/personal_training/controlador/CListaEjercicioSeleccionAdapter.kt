package com.example.personal_training.controlador

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.modelo.Ejercicio

class CListaEjercicioSeleccionAdapter(
    private val listaEjercicios: List<Ejercicio>,
    private val ejerciciosSeleccionados: MutableSet<Int>,
    private val diasSeleccionados: MutableMap<Int, List<String>>,
    private val listaDias: Array<String>
) : RecyclerView.Adapter<CListaEjercicioSeleccionAdapter.EjercicioSeleccionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioSeleccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_ejercicio_seleccion, parent, false)
        return EjercicioSeleccionViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioSeleccionViewHolder, position: Int) {
        val ejercicio = listaEjercicios[position]
        holder.nombreEjercicioTextView.text = ejercicio.nombre
        holder.checkboxSeleccionarEjercicio.isChecked = ejerciciosSeleccionados.contains(ejercicio.id)

        holder.checkboxSeleccionarEjercicio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ejerciciosSeleccionados.add(ejercicio.id!!)
            } else {
                ejerciciosSeleccionados.remove(ejercicio.id!!)
            }
        }
        holder.seleccionDiasTextView.setOnClickListener {
            mostrarDialogoSeleccionDias(holder.itemView.context, ejercicio.id!!, holder.seleccionDiasTextView)
        }
        val diasSeleccionadosParaEjercicio = diasSeleccionados[ejercicio.id]
        if (diasSeleccionadosParaEjercicio != null) {
            holder.seleccionDiasTextView.text = diasSeleccionadosParaEjercicio.joinToString(", ")
        }
    }

    private fun mostrarDialogoSeleccionDias(context: Context, ejercicioId: Int, textView: TextView) {
        val diasSeleccionadosPorEjercicio = diasSeleccionados[ejercicioId]?.toMutableList() ?: mutableListOf()

        val diasSeleccionadosBool = listaDias.map { diasSeleccionadosPorEjercicio.contains(it) }.toBooleanArray()

        AlertDialog.Builder(context)
            .setTitle("Seleccionar días")
            .setMultiChoiceItems(listaDias, diasSeleccionadosBool) { _, which, isChecked ->
                if (isChecked) {
                    diasSeleccionadosPorEjercicio.add(listaDias[which])
                } else {
                    diasSeleccionadosPorEjercicio.remove(listaDias[which])
                }
            }
            .setPositiveButton("OK") { _, _ ->
                diasSeleccionados[ejercicioId] = diasSeleccionadosPorEjercicio
                textView.text = diasSeleccionadosPorEjercicio.joinToString(", ")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun getItemCount() = listaEjercicios.size

    inner class EjercicioSeleccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxSeleccionarEjercicio: CheckBox = itemView.findViewById(R.id.checkbox_seleccionar_ejercicio)
        val nombreEjercicioTextView: TextView = itemView.findViewById(R.id.nombre_ejercicio)
        val seleccionDiasTextView: TextView = itemView.findViewById(R.id.seleccion_dias)
    }
}
