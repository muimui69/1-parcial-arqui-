package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class CrearRutinaCommand (
    private val mrutina: MRutina,
    private val rutina: Rutina
) : Command {

    override fun execute() {
        mrutina.crearRutina(rutina)
        println("Rutina creada: ${rutina.nombre}")
    }
}
