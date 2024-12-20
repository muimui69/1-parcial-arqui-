package com.example.personal_training.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(contexto: Context) : SQLiteOpenHelper(contexto, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "personal_training.db"
        const val DATABASE_VERSION = 1

        const val TABLE_CLIENTE = "cliente"
        const val TABLE_DIETA = "dieta"
        const val TABLE_RUTINA = "rutina"
        const val TABLE_EJERCICIO = "ejercicio"
        const val TABLE_RUTINA_EJERCICIO = "rutina_ejercicio"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE_CLIENTE = """
             CREATE TABLE IF NOT EXISTS $TABLE_CLIENTE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                altura REAL NOT NULL,
                correo TEXT NOT NULL,
                nombre TEXT NOT NULL,
                peso REAL NOT NULL,
                sexo TEXT NOT NULL,
                telefono TEXT NOT NULL
            );
        """.trimIndent()

        val CREATE_TABLE_DIETA = """
            CREATE TABLE IF NOT EXISTS $TABLE_DIETA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL
            );
        """.trimIndent()

        val CREATE_TABLE_RUTINA = """
             CREATE TABLE IF NOT EXISTS $TABLE_RUTINA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                tipo TEXT NOT NULL,
                cliente_id INTEGER NOT NULL,
                dieta_id INTEGER NOT NULL,
                FOREIGN KEY (cliente_id) REFERENCES $TABLE_CLIENTE(id),
                FOREIGN KEY (dieta_id) REFERENCES $TABLE_DIETA(id)
            );
        """.trimIndent()

        val CREATE_TABLE_EJERCICIO = """
            CREATE TABLE IF NOT EXISTS $TABLE_EJERCICIO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                imagen_url TEXT,
                duracion TEXT NOT NULL,
                nombre TEXT NOT NULL,
                repeticion TEXT NOT NULL
            );
        """.trimIndent()

        val CREATE_TABLE_RUTINA_EJERCICIO = """
            CREATE TABLE IF NOT EXISTS $TABLE_RUTINA_EJERCICIO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dia_rutina TEXT NOT NULL,
                rutina_id INTEGER NOT NULL,
                ejercicio_id INTEGER NOT NULL,
                FOREIGN KEY (rutina_id) REFERENCES $TABLE_RUTINA(id),
                FOREIGN KEY (ejercicio_id) REFERENCES $TABLE_EJERCICIO(id)
            );
        """.trimIndent()

        db.execSQL(CREATE_TABLE_CLIENTE)
        db.execSQL(CREATE_TABLE_DIETA)
        db.execSQL(CREATE_TABLE_RUTINA)
        db.execSQL(CREATE_TABLE_EJERCICIO)
        db.execSQL(CREATE_TABLE_RUTINA_EJERCICIO)

        Log.d("DatabaseHelper", "Tablas creadas con éxito.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RUTINA_EJERCICIO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EJERCICIO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RUTINA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIETA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTE")
        onCreate(db)
    }

}

