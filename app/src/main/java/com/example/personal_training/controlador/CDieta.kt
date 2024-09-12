package com.example.personal_training.controlador


import android.content.Context
import com.example.personal_training.modelo.MDieta
import com.example.personal_training.modelo.Dieta

class CPlanDieta(context: Context) {
    private val modeloDieta = MDieta(context)

    fun crearPlanDieta(dieta: Dieta): Long {
        return modeloDieta.crearDieta(dieta)
    }

    fun actualizarPlanDieta(dieta: Dieta): Int {
        return modeloDieta.actualizarDieta(dieta)
    }

    fun eliminarPlanDieta(id: Int): Int {
        return modeloDieta.eliminarDieta(id)
    }

    fun obtenerPlanesDieta(): List<Dieta> {
        return modeloDieta.obtenerDietas()
    }
}
