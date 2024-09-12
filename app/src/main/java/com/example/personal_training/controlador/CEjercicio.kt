package com.example.personal_training.controlador

import android.content.Context
import com.example.personal_training.modelo.MEjercicio
import com.example.personal_training.modelo.Ejercicio

class CEjercicio(context: Context) {
    private val modeloEjercicio = MEjercicio(context)

    fun crearEjercicio(ejercicio: Ejercicio): Long {
        return modeloEjercicio.crearEjercicio(ejercicio)
    }

    fun actualizarEjercicio(ejercicio: Ejercicio): Int {
        return modeloEjercicio.actualizarEjercicio(ejercicio)
    }

    fun eliminarEjercicio(id: Int): Int {
        return modeloEjercicio.eliminarEjercicio(id)
    }

    fun obtenerEjercicios(): List<Ejercicio> {
        return modeloEjercicio.obtenerEjercicios()
    }
}
