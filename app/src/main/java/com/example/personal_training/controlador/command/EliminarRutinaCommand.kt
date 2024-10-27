package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina

class EliminarRutinaCommand(
    private val mrutina: MRutina,
    private val rutinaId: Int
) : Command {

    override fun execute() {
        mrutina.eliminarRutina(rutinaId)
        println("Rutina eliminada con ID: $rutinaId")
    }
}
