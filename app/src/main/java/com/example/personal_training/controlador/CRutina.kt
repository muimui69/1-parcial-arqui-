package com.example.personal_training.controlador

import android.content.Context
import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class CRutina(context: Context) {
    private val modeloRutina = MRutina(context)

    fun crearRutina(rutina: Rutina): Long {
        return modeloRutina.crearRutina(rutina)
    }

    fun actualizarRutina(rutina: Rutina): Int {
        return modeloRutina.actualizarRutina(rutina)
    }

    fun eliminarRutina(id: Int): Int {
        return modeloRutina.eliminarRutina(id)
    }

    fun obtenerRutinas(): List<Rutina> {
        return modeloRutina.obtenerRutinas()
    }
}
