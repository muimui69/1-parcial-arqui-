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
    var imagen_url: String
)

class MEjercicio(contexto: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(contexto)
    val db: SQLiteDatabase = dbHelper.writableDatabase

    fun crearEjercicio(ejercicio: Ejercicio): Long {
        val valores = ContentValues().apply {
            put("nombre", ejercicio.nombre)
            put("duracion", ejercicio.duracion)
            put("repeticion", ejercicio.repeticion)
            put("imagen_url", ejercicio.imagen_url)
        }
        return db.insert("ejercicio", null, valores)
    }

    fun obtenerEjercicio(id: Int): Ejercicio? {
        val cursor = db.query("ejercicio", null, "id = ?", arrayOf(id.toString()), null, null, null)

        var ejercicio: Ejercicio? = null
        if (cursor.moveToFirst()) {
            ejercicio =  Ejercicio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                repeticion = cursor.getString(cursor.getColumnIndexOrThrow("repeticion")),
                duracion = cursor.getString(cursor.getColumnIndexOrThrow("duracion")),
                imagen_url = cursor.getString(cursor.getColumnIndexOrThrow("imagen_url"))
            )
        }
        cursor.close()
        return ejercicio
    }

    fun obtenerEjercicios(): List<Ejercicio> {
        val cursor = db.query("ejercicio", null, null, null, null, null, null)
        val listaEjercicios = mutableListOf<Ejercicio>()
        while (cursor.moveToNext()) {
            val ejercicio = Ejercicio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                duracion = cursor.getString(cursor.getColumnIndexOrThrow("duracion")),
                repeticion = cursor.getString(cursor.getColumnIndexOrThrow("repeticion")),
                imagen_url = cursor.getString(cursor.getColumnIndexOrThrow("imagen_url"))
            )
            listaEjercicios.add(ejercicio)
        }
        cursor.close()
        return listaEjercicios
    }

    fun actualizarEjercicio(ejercicio: Ejercicio): Int {
        val valores = ContentValues().apply {
            put("nombre", ejercicio.nombre)
            put("duracion", ejercicio.duracion)
            put("repeticion", ejercicio.repeticion)
            put("imagen_url", ejercicio.imagen_url)
        }
        return db.update("ejercicio", valores, "id = ?", arrayOf(ejercicio.id.toString()))
    }

    fun eliminarEjercicio(id: Int): Int {
        return db.delete("ejercicio", "id = ?", arrayOf(id.toString()))
    }


}
