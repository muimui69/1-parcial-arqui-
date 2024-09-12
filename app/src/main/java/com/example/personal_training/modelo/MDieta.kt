package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class Dieta(
    var id: Int? = null,
    var descripcion: String
)

class MDieta(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun crearDieta(dieta: Dieta): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("descripcion", dieta.descripcion)
        }
        return db.insert("plan_dieta", null, valores)
    }

    fun actualizarDieta(dieta: Dieta): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put("descripcion", dieta.descripcion)
        }
        return db.update("plan_dieta", valores, "id = ?", arrayOf(dieta.id.toString()))
    }

    fun eliminarDieta(id: Int): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        return db.delete("plan_dieta", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerDietas(): List<Dieta> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query("plan_dieta", null, null, null, null, null, null)
        val listaPlanesDieta = mutableListOf<Dieta>()
        while (cursor.moveToNext()) {
            val planDieta = Dieta(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            )
            listaPlanesDieta.add(planDieta)
        }
        cursor.close()
        return listaPlanesDieta
    }
}
