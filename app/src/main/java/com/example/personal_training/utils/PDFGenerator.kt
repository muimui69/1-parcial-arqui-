package com.example.personal_training.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.Rutina
import com.example.personal_training.modelo.RutinaEjercicio
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PDFGenerator {

    fun generarPDFRutina(
        context: Context,
        cliente: Cliente,
        rutina: Rutina,
        dieta: Dieta,
        rutinaEjerciciosFiltrados: List<RutinaEjercicio>,
        ejercicios: List<Ejercicio>
    ): String? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = document.startPage(pageInfo)

        var canvas = page.canvas
        val paint = android.graphics.Paint()
        var currentY = 50f

        canvas.drawText("Rutina de entrenamiento", 100f, currentY, paint)
        currentY += 50f
        canvas.drawText("Cliente: ${cliente.nombre}", 100f, currentY, paint)
        currentY += 30f
        canvas.drawText("Teléfono: ${cliente.telefono}", 100f, currentY, paint)
        currentY += 30f
        canvas.drawText("Correo: ${cliente.correo}", 100f, currentY, paint)

        currentY += 50f
        canvas.drawText("Rutina: ${rutina.nombre}", 100f, currentY, paint)
        currentY += 30f
        canvas.drawText("Tipo de rutina: ${rutina.tipo}", 100f, currentY, paint)

        currentY += 50f
        canvas.drawText("Ejercicios:", 100f, currentY, paint)
        currentY += 30f

        val ejerciciosAgrupados = rutinaEjerciciosFiltrados.groupBy { it.ejercicio_id }
        val listaEjerciciosConDias = ejerciciosAgrupados.map { (ejercicioId, rutinaEjercicios) ->
            val ejercicio = ejercicios.find { it.id == ejercicioId }
            val dias = rutinaEjercicios.joinToString(", ") { it.dia_rutina }
            Pair(ejercicio, dias)
        }

        for ((ejercicio, dias) in listaEjerciciosConDias) {
            if (ejercicio != null) {
                canvas.drawText("Ejercicio: ${ejercicio.nombre}", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Días: $dias", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Repeticiones: ${ejercicio.repeticion}", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Duración: ${ejercicio.duracion}", 100f, currentY, paint)
                currentY += 30f

                // Verificar si el ejercicio tiene una imagen
                val imagePath = ejercicio.imagen_url
                if (!imagePath.isNullOrEmpty()) {
                    val imgFile = File(imagePath)
                    if (imgFile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                        currentY += 20f // Espacio extra antes de la imagen
                        canvas.drawBitmap(scaledBitmap, 100f, currentY, paint)
                        currentY += 120f
                    }
                }

                // Verificar si es necesario crear una nueva página
                if (currentY > pageInfo.pageHeight - 100) {
                    document.finishPage(page)
                    page = document.startPage(pageInfo)
                    canvas = page.canvas
                    currentY = 50f
                }
            }
        }

        // Información de la dieta
        canvas.drawText("Dieta: ${dieta.titulo}", 100f, currentY, paint)
        currentY += 30f
        canvas.drawText("Descripción: ${dieta.descripcion}", 100f, currentY, paint)

        document.finishPage(page)

        val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val pdfFilePath = "$directoryPath/rutina_${cliente.nombre}.pdf"
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
