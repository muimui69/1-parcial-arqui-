package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class CrearRutinaCommand(
    private val mrutina: MRutina,
    private val rutina: Rutina
) : Command {

    internal var rutinaId: Long? = null

    override fun execute() {
        rutinaId = mrutina.crearRutina(rutina)
        if (rutinaId != null && rutinaId!! > 0) {
            println("Rutina creada: ${rutina.nombre} con ID: $rutinaId")
        } else {
            println("Error al crear la rutina: ${rutina.nombre}")
        }
    }

    override fun undo() {
        if (rutinaId != null && rutinaId!! > 0) {
            mrutina.eliminarRutina(rutinaId!!.toInt())
            println("Rutina eliminada: ${rutina.nombre} con ID: $rutinaId")
        } else {
            println("No hay ninguna rutina creada para deshacer.")
        }
    }
}
