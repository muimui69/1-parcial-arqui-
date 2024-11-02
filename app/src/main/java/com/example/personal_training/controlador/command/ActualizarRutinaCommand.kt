package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class ActualizarRutinaCommand(
    private val mrutina: MRutina,
    private val rutina: Rutina
) : Command {

    private var rutinaAnterior: Rutina? = null

    override fun execute() {
        rutinaAnterior = mrutina.obtenerRutina(rutina.id!!)
        mrutina.actualizarRutina(rutina)
        println("Rutina actualizada: ${rutina.nombre}")
    }

    override fun undo() {
        if (rutinaAnterior != null) {
            mrutina.actualizarRutina(rutinaAnterior!!)
            println("Rutina revertida a su estado anterior: ${rutinaAnterior?.nombre}")
        } else {
            println("No hay estado anterior para deshacer la actualización.")
        }
    }
}
