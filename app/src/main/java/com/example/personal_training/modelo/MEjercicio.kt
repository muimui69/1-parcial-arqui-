package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class Ejercicio(
    var id: Int? = null,
    var nombre: String,
    var duracion: String,
    var repeticion: String,
    var archivoMultimedia: String
)

class MEjercicio(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun crearEjercicio(ejercicio: Ejercicio): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", ejercicio.nombre)
            put("duracion", ejercicio.duracion)
            put("repeticion", ejercicio.repeticion)
            put("archivo_multimedia", ejercicio.archivoMultimedia)
        }
        return db.insert("ejercicio", null, valores)
    }

    fun actualizarEjercicio(ejercicio: Ejercicio): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", ejercicio.nombre)
            put("duracion", ejercicio.duracion)
            put("repeticion", ejercicio.repeticion)
            put("archivo_multimedia", ejercicio.archivoMultimedia)
        }
        return db.update("ejercicio", valores, "id = ?", arrayOf(ejercicio.id.toString()))
    }

    fun eliminarEjercicio(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        return db.delete("ejercicio", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerEjercicios(): List<Ejercicio> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query("ejercicio", null, null, null, null, null, null)
        val listaEjercicios = mutableListOf<Ejercicio>()
        while (cursor.moveToNext()) {
            val ejercicio = Ejercicio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                duracion = cursor.getString(cursor.getColumnIndexOrThrow("duracion")),
                repeticion = cursor.getString(cursor.getColumnIndexOrThrow("repeticion")),
                archivoMultimedia = cursor.getString(cursor.getColumnIndexOrThrow("archivo_multimedia"))
            )
            listaEjercicios.add(ejercicio)
        }
        cursor.close()
        return listaEjercicios
    }
}
