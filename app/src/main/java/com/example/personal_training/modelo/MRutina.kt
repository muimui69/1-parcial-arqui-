package com.example.personal_training.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.personal_training.db.DatabaseHelper

data class Rutina(
    var id: Int? = null,
    var nombre: String,
    var tipo: String,
    var cliente_id: Int,
    var dieta_id: Int
)

class MRutina(contexto: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(contexto)
    val db: SQLiteDatabase = dbHelper.writableDatabase

    fun crearRutina(rutina: Rutina): Long {
        val valores = ContentValues().apply {
            put("nombre", rutina.nombre)
            put("tipo", rutina.tipo)
            put("cliente_id", rutina.cliente_id)
            put("dieta_id", rutina.dieta_id)
        }
        return db.insert("rutina", null, valores)
    }

    fun obtenerRutina(id: Int): Rutina? {
        val cursor = db.query("rutina",  null, "id = ?", arrayOf(id.toString()), null,  null,  null   )

        var rutina: Rutina? = null
        if (cursor.moveToFirst()) {
            rutina = Rutina(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cliente_id = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                dieta_id = cursor.getInt(cursor.getColumnIndexOrThrow("dieta_id")),
            )
        }
        cursor.close()
        return rutina
    }

    fun obtenerRutinas(): List<Rutina> {
        val cursor = db.query("rutina", null, null, null, null, null, null)
        val listaRutinas = mutableListOf<Rutina>()
        while (cursor.moveToNext()) {
            val rutina = Rutina(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cliente_id = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                dieta_id = cursor.getInt(cursor.getColumnIndexOrThrow("dieta_id")),
            )
            listaRutinas.add(rutina)
        }
        cursor.close()
        return listaRutinas
    }

    fun actualizarRutina(rutina: Rutina): Int {
        val valores = ContentValues().apply {
            put("nombre", rutina.nombre)
            put("tipo", rutina.tipo)
            put("cliente_id", rutina.cliente_id)
            put("dieta_id", rutina.dieta_id)
        }
        return db.update("rutina", valores, "id = ?", arrayOf(rutina.id.toString()))
    }

    fun eliminarRutina(id: Int): Int {
        return db.delete("rutina", "id = ?", arrayOf(id.toString()))
    }

    fun obtenerRutinasConClientes(clientesIds: List<Int?>): List<Rutina> {
        val listaRutinas = mutableListOf<Rutina>()

        if (clientesIds.isEmpty()) return listaRutinas

        val idsString = clientesIds.joinToString(separator = ",")

        val query = """
        SELECT rutina.* FROM rutina
        WHERE cliente_id IN ($idsString)
    """.trimIndent()

        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val rutina = Rutina(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cliente_id = cursor.getInt(cursor.getColumnIndexOrThrow("cliente_id")),
                dieta_id = cursor.getInt(cursor.getColumnIndexOrThrow("dieta_id"))
            )

            listaRutinas.add(rutina)
        }

        cursor.close()
        return listaRutinas
    }

}
