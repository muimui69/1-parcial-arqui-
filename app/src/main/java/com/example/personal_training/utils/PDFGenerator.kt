//package com.example.personal_training.utils
//
//import android.content.Context
//import android.graphics.pdf.PdfDocument
//import android.os.Environment
//import android.util.Log
//import com.example.personal_training.modelo.Cliente
//import com.example.personal_training.modelo.Dieta
//import com.example.personal_training.modelo.Ejercicio
//import com.example.personal_training.modelo.MEjercicio
//import com.example.personal_training.modelo.Rutina
//import com.example.personal_training.modelo.RutinaEjercicio
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//object PDFGenerator {
//
////    fun generarPDFRutina(context: Context, cliente: Cliente, rutina: Rutina): String? {
////        val document = PdfDocument()
////
////        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
////        val page = document.startPage(pageInfo)
////
////        val canvas = page.canvas
////        val paint = android.graphics.Paint()
////
////        canvas.drawText("Rutina de entrenamiento", 100f, 50f, paint)
////        canvas.drawText("Cliente: ${cliente.nombre}", 100f, 100f, paint)
////        canvas.drawText("Teléfono: ${cliente.telefono}", 100f, 150f, paint)
////        canvas.drawText("Correo: ${cliente.correo}", 100f, 200f, paint)
////        canvas.drawText("Tipo de rutina: ${rutina.tipo}", 100f, 250f, paint)
////
////        document.finishPage(page)
////
////        val directoryPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
////        val pdfFilePath = "$directoryPath/rutina_${cliente.nombre}.pdf"
////        val file = File(pdfFilePath)
////
////        try {
////            FileOutputStream(file).use { out ->
////                document.writeTo(out)
////                Log.d("PDFGenerator", "PDF creado en $pdfFilePath")
////            }
////        } catch (e: IOException) {
////            e.printStackTrace()
////            Log.e("PDFGenerator", "Error al generar el PDF", e)
////            return null
////        } finally {
////            document.close()
////        }
////
////        return pdfFilePath
////    }
//
//
//
//
//}


package com.example.personal_training.utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.MEjercicio
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
        listaEjercicios: List<RutinaEjercicio>,
        dieta: Dieta,
        mejercicio: MEjercicio
    ): String? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()

        // Información del cliente
        canvas.drawText("Rutina de entrenamiento", 100f, 50f, paint)
        canvas.drawText("Cliente: ${cliente.nombre}", 100f, 100f, paint)
        canvas.drawText("Teléfono: ${cliente.telefono}", 100f, 150f, paint)
        canvas.drawText("Correo: ${cliente.correo}", 100f, 200f, paint)

        // Información de la rutina
        canvas.drawText("Rutina: ${rutina.nombre}", 100f, 250f, paint)
        canvas.drawText("Tipo de rutina: ${rutina.tipo}", 100f, 300f, paint)

        var currentY = 350f
        canvas.drawText("Ejercicios:", 100f, currentY, paint)
        currentY += 50f

        // Mostrar los ejercicios y días
        for (rutinaEjercicio in listaEjercicios) {
            val ejercicio = mejercicio.obtenerEjercicio(rutinaEjercicio.ejercicio_id)
            if (ejercicio != null) {
                canvas.drawText("Ejercicio: ${ejercicio.nombre}", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Días: ${rutinaEjercicio.dia_rutina}", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Repeticiones: ${ejercicio.repeticion}", 100f, currentY, paint)
                currentY += 30f
                canvas.drawText("Duración: ${ejercicio.duracion}", 100f, currentY, paint)
                currentY += 50f
            }
        }

        // Información de la dieta
        canvas.drawText("Dieta: ${dieta.titulo}", 100f, currentY, paint)
        currentY += 30f
        canvas.drawText("Descripción: ${dieta.descripcion}", 100f, currentY, paint)

        // Finalizar la página
        document.finishPage(page)

        // Guardar el PDF en el almacenamiento externo
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

