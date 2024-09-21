package com.example.personal_training.controlador

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.personal_training.R
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.Rutina
import com.example.personal_training.utils.PDFGenerator
import java.io.File

class CListaEnviarRutinaAdapter(
    private var listaRutinas: List<Rutina>,
    private val clientes: Map<Int, Cliente>,
    private val dietas: Map<Int, Dieta>,
    private val ejercicios: Map<Int, Ejercicio>
) : RecyclerView.Adapter<CListaEnviarRutinaAdapter.EnviarRutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnviarRutinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.v_item_enviar_rutina, parent, false)
        return EnviarRutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnviarRutinaViewHolder, position: Int) {
        val rutina = listaRutinas[position]
        val cliente = clientes[rutina.cliente_id]

        holder.nombreCliente.text = cliente?.nombre ?: "Cliente no encontrado"
        holder.nombreRutina.text = rutina.nombre

        holder.btnVerPdf.setOnClickListener {
            val pdfFilePath = PDFGenerator.generarPDFRutina(holder.itemView.context, cliente!!, rutina,ejercicios,dietas)

            if (pdfFilePath != null) {
                val file = File(pdfFilePath)
                val pdfUri = FileProvider.getUriForFile(
                    holder.itemView.context,
                    "${holder.itemView.context.packageName}.provider",
                    file
                )

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(pdfUri, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                try {
                    holder.itemView.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(holder.itemView.context, "No hay aplicaciones para ver PDF", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(holder.itemView.context, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnEnviarPorWpp.setOnClickListener {
            val pdfFilePath = PDFGenerator.generarPDFRutina(holder.itemView.context, cliente!!, rutina,ejercicios,dietas)
            val pdfFile = File(pdfFilePath)

            val uri = FileProvider.getUriForFile(
                holder.itemView.context,
                "${holder.itemView.context.packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.setPackage("com.whatsapp")

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(holder.itemView.context, "WhatsApp no está instalado", Toast.LENGTH_LONG).show()
            }
        }

        holder.btnEnviarPorCorreo.setOnClickListener {
            val pdfFilePath = PDFGenerator.generarPDFRutina(holder.itemView.context, cliente!!, rutina,ejercicios,dietas)


            if (pdfFilePath != null) {
                val file = File(pdfFilePath)
                val pdfUri = FileProvider.getUriForFile(
                    holder.itemView.context,
                    "${holder.itemView.context.packageName}.provider",
                    file
                )

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(cliente.correo))
                    putExtra(Intent.EXTRA_SUBJECT, "Rutina de Entrenamiento")
                    putExtra(Intent.EXTRA_TEXT, "Adjunto la rutina de entrenamiento.")
                    putExtra(Intent.EXTRA_STREAM, pdfUri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                try {
                    holder.itemView.context.startActivity(Intent.createChooser(intent, "Enviar correo"))
                } catch (e: Exception) {
                    Toast.makeText(holder.itemView.context, "No se puede enviar el correo", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(holder.itemView.context, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount() = listaRutinas.size

    inner class EnviarRutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreCliente: TextView = itemView.findViewById(R.id.nombre_cliente)
        val nombreRutina: TextView = itemView.findViewById(R.id.nombre_rutina)
        val btnVerPdf: Button = itemView.findViewById(R.id.btn_ver_pdf)
        val btnEnviarPorWpp: Button = itemView.findViewById(R.id.btn_enviar_por_wpp)
        val btnEnviarPorCorreo: Button = itemView.findViewById(R.id.btn_enviar_por_correo)
    }
}

