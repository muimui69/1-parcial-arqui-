package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class Dieta(
    var id: Int? = null,
    var titulo: String,
    var descripcion: String
)

class MDieta(contexto: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(contexto)
    val db: SQLiteDatabase = dbHelper.writableDatabase

    fun crearDieta(dieta: Dieta): Long {
        val valores = ContentValues().apply {
            put("titulo", dieta.titulo)
            put("descripcion", dieta.descripcion)
        }
        return db.insert("dieta", null, valores)
    }

    fun obtenerDieta(id: Int): Dieta? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query("dieta",null,"id = ?",arrayOf(id.toString()), null, null,null)

        var dieta: Dieta? = null
        if (cursor.moveToFirst()) {
            dieta = Dieta(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            )
        }
        cursor.close()
        return dieta
    }

    fun obtenerDietas(): List<Dieta> {
        val cursor = db.query("dieta", null, null, null, null, null, null)
        val listaDietas = mutableListOf<Dieta>()
        while (cursor.moveToNext()) {
            val dieta = Dieta(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            )
            listaDietas.add(dieta)
        }
        cursor.close()
        return listaDietas
    }

    fun actualizarDieta(dieta: Dieta): Int {
        val valores = ContentValues().apply {
            put("titulo", dieta.titulo)
            put("descripcion", dieta.descripcion)
        }
        return db.update("dieta", valores, "id = ?", arrayOf(dieta.id.toString()))
    }

    fun eliminarDieta(id: Int): Int {
        return db.delete("dieta", "id = ?", arrayOf(id.toString()))
    }

}
