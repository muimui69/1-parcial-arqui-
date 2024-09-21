package com.example.personal_training.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.Rutina
import com.example.personal_training.modelo.Ejercicio
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PDFGenerator {

    fun generarPDFRutina(
        context: Context,
        cliente: Cliente,
        rutina: Rutina,
        listaEjercicios: Map<Int, Ejercicio>,
        dieta: Map<Int, Dieta>
    ): String? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        canvas.drawText("Rutina de entrenamiento", 100f, 50f, paint)
        canvas.drawText("Cliente: ${cliente.nombre}", 100f, 100f, paint)
        canvas.drawText("Teléfono: ${cliente.telefono}", 100f, 150f, paint)
        canvas.drawText("Correo: ${cliente.correo}", 100f, 200f, paint)

        canvas.drawText("Rutina: ${rutina.nombre}", 100f, 250f, paint)
        canvas.drawText("Tipo de rutina: ${rutina.tipo}", 100f, 300f, paint)

        var currentY = 350f
        canvas.drawText("Ejercicios:", 100f, currentY, paint)
        currentY += 50f

        val ejerciciosSeleccionados = listaEjercicios.filterKeys { it == cliente.id }
        for ((_, ejercicio) in ejerciciosSeleccionados) {
            canvas.drawText("Ejercicio: ${ejercicio.nombre}", 100f, currentY, paint)

            val imagePath = ejercicio.imagen_url
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap != null) {
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                canvas.drawBitmap(scaledBitmap, 350f, currentY - 30f, paint) // Coloca la imagen a la derecha del texto
            } else {
                canvas.drawText("Imagen no disponible", 350f, currentY, paint)
            }

            currentY += 30f
            canvas.drawText("Repeticiones: ${ejercicio.repeticion}", 100f, currentY, paint)
            currentY += 30f
            canvas.drawText("Duración: ${ejercicio.duracion}", 100f, currentY, paint)
            currentY += 50f
        }

        val dietaSeleccionada = dieta[cliente.id]
        if (dietaSeleccionada != null) {
            canvas.drawText("Dieta: ${dietaSeleccionada.titulo}", 100f, currentY, paint)
            currentY += 30f
            canvas.drawText("Descripción: ${dietaSeleccionada.descripcion}", 100f, currentY, paint)
        }

        document.finishPage(page)

        val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val pdfFilePath = "$directoryPath/rutina_${cliente.nombre}_${System.currentTimeMillis()}.pdf"
        val file = File(pdfFilePath)

        try {
            FileOutputStream(file).use { out ->
                document.writeTo(out)
                Log.d("PDFGenerator", "PDF creado en $pdfFilePath")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("PDFGenerator", "Error al generar el PDF", e)
            return null
        } finally {
            document.close()
        }

        return pdfFilePath
    }
}
