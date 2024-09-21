package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class RutinaEjercicio(
    var id: Int? = null,
    var dia_rutina: String,
    var rutina_id: Int,
    var ejercicio_id: Int
)

class MRutinaEjercicio(contexto: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(contexto)
    val db: SQLiteDatabase = dbHelper.writableDatabase

    fun asociarEjercicioARutina(rutinaEjercicio:RutinaEjercicio): Long {
        val valores = ContentValues().apply {
            put("rutina_id", rutinaEjercicio.rutina_id)
            put("ejercicio_id", rutinaEjercicio.ejercicio_id)
            put("dia_rutina", rutinaEjercicio.dia_rutina)
        }
        return db.insert("rutina_ejercicio", null, valores)
    }

    fun obtenerEjerciciosPorRutina(rutinaId: Int): List<Pair<Int, String>> {
        val lista = mutableListOf<Pair<Int, String>>()
        val cursor = db.query("rutina_ejercicio", arrayOf("ejercicio_id", "dia_rutina"), "rutina_id = ?", arrayOf(rutinaId.toString()), null, null, null)
        while (cursor.moveToNext()) {
            val ejercicioId = cursor.getInt(cursor.getColumnIndexOrThrow("ejercicio_id"))
            val diaRutina = cursor.getString(cursor.getColumnIndexOrThrow("dia_rutina"))
            lista.add(Pair(ejercicioId, diaRutina))
        }
        cursor.close()
        return lista
    }

    fun obtenerTodasRutinaEjercicio(): List<RutinaEjercicio> {
        val lista = mutableListOf<RutinaEjercicio>()
        val cursor = db.query("rutina_ejercicio", null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val rutinaEjercicio = RutinaEjercicio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                rutina_id = cursor.getInt(cursor.getColumnIndexOrThrow("rutina_id")),
                ejercicio_id = cursor.getInt(cursor.getColumnIndexOrThrow("ejercicio_id")),
                dia_rutina = cursor.getString(cursor.getColumnIndexOrThrow("dia_rutina"))
            )
            lista.add(rutinaEjercicio)
        }
        cursor.close()
        return lista
    }

    fun eliminarEjerciciosPorRutina(rutinaId: Int): Int {
        return db.delete("rutina_ejercicio", "rutina_id = ?", arrayOf(rutinaId.toString()))
    }

}
