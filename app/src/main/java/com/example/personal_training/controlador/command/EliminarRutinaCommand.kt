package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class EliminarRutinaCommand(
    private val mrutina: MRutina,
    private val rutinaId: Int,
) : Command {

    private var backup: Rutina? = null

    override fun execute() {
        val resultado = mrutina.eliminarRutina(rutinaId)
        if (resultado > 0) {
            println("Rutina eliminada con ID: $rutinaId")
        } else {
            println("Error al eliminar la rutina con ID: $rutinaId")
        }
    }

    override fun undo() {
        backup?.let { mrutina.crearRutina(it) }
        println("Rutina restaurada con ID: ${backup?.id}")
    }
}
