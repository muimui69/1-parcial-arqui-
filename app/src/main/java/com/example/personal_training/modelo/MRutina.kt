package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class Rutina(
    var id: Int? = null,
    var fechaCreacion: String,
    var nombre: String,
    var tipo: String
)

class MRutina(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun crearRutina(rutina: Rutina): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("fecha_creacion", rutina.fechaCreacion)
            put("nombre", rutina.nombre)
            put("tipo", rutina.tipo)
        }
        return db.insert("rutina", null, valores)
    }

    fun actualizarRutina(rutina: Rutina): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("fecha_creacion", rutina.fechaCreacion)
            put("nombre", rutina.nombre)
            put("tipo", rutina.tipo)
        }
        return db.update("rutina", valores, "id = ?", arrayOf(rutina.id.toString()))
    }

    fun eliminarRutina(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        return db.delete("rutina", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerRutinas(): List<Rutina> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query("rutina", null, null, null, null, null, null)
        val listaRutinas = mutableListOf<Rutina>()
        while (cursor.moveToNext()) {
            val rutina = Rutina(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
            )
            listaRutinas.add(rutina)
        }
        cursor.close()
        return listaRutinas
    }
}
