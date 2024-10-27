package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class ActualizarRutinaCommand(
    private val mrutina: MRutina,
    private val rutina: Rutina
) : Command {

    override fun execute() {
        mrutina.actualizarRutina(rutina)
        println("Rutina actualizada: ${rutina.nombre}")
    }
}
