package com.example.personal_training.controlador

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.MEjercicio
import com.google.android.material.snackbar.Snackbar
import java.io.File

class CListaEjercicioAdapter (
    private val listaEjercicios: MutableList<Ejercicio>,
    private val mejercicio: MEjercicio
) : RecyclerView.Adapter<CListaEjercicioAdapter.EjercicioViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_ejercicio, parent, false)
        return EjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicio = listaEjercicios[position]

        holder.nombreEjercicioTextView.text = ejercicio.nombre
        holder.duracionEjercicioTextView.text = ejercicio.duracion
        holder.repetecionEjercicioTextView.text = ejercicio.repeticion

        cargarImagenEjercicio(ejercicio.imagen_url, holder.imagenEjercicioImageView)

        holder.btnEditar.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("ejercicioId", ejercicio.id!!)
            }
            holder.itemView.findNavController().navigate(R.id.action_nav_ejercicios_to_editarEjercicioFragment, bundle)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Ejercicio")
                .setMessage("¿Estás seguro de que deseas eliminar el ejercicio '${ejercicio.nombre}'?")
                .setPositiveButton("Sí") { _, _ ->
                    val resultado = mejercicio.eliminarEjercicio(ejercicio.id!!)
                    if (resultado > 0) {
                        listaEjercicios.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                        Snackbar.make(it, "Ejercicio eliminado", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(it, "Error al eliminar el ejercicio", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }


    private fun cargarImagenEjercicio(rutaImagen: String?, imageView: ImageView) {
        if (!rutaImagen.isNullOrEmpty()) {
            val imgFile = File(rutaImagen)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imageView.setImageBitmap(bitmap)
            } else {
                Log.d("ErrorImagen", "La imagen no existe en la ruta: $rutaImagen")
                // Usa una imagen por defecto si no se puede cargar la imagen
//                imageView.setImageResource(R.drawable.imagen_no_disponible)
            }
        } else {
            // Usa una imagen por defecto si no hay imagen proporcionada
//            imageView.setImageResource(R.drawable.imagen_no_disponible)
        }
    }



    override fun getItemCount() = listaEjercicios.size

    inner class EjercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEjercicioTextView: TextView = itemView.findViewById(R.id.nombre_ejercicio)
        val duracionEjercicioTextView: TextView = itemView.findViewById(R.id.duracion_ejercicio)
        val repetecionEjercicioTextView: TextView = itemView.findViewById(R.id.repeticion_ejercicio)
        val imagenEjercicioImageView: ImageView = itemView.findViewById(R.id.imagen_ejercicio)
        val btnEditar: Button = itemView.findViewById(R.id.btn_editar_ejercicio)
        val btnEliminar: Button = itemView.findViewById(R.id.btn_eliminar_ejercicio)
    }

}
