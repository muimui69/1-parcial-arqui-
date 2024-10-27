package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina

class ObtenerRutinaCommand(
    private val mrutina: MRutina,
    private val rutinaId: Int
) : Command {

    override fun execute() {
        val rutina = mrutina.obtenerRutina(rutinaId)
        if (rutina != null) {
            println("Rutina obtenida: ${rutina.nombre}, Tipo: ${rutina.tipo}")
        } else {
            println("No se encontró ninguna rutina con ID: $rutinaId")
        }
    }
}
