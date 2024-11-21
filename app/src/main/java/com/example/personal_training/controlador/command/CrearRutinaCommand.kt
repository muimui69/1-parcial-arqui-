package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class CrearRutinaCommand(
    private val mrutina: MRutina,
    private val rutina: Rutina
) : Command {

    var rutinaId: Long? = null

    override fun execute() {
        // Intentar crear la rutina
        rutinaId = mrutina.crearRutina(rutina)
        if (rutinaId != null && rutinaId!! > 0) {
            println("Rutina creada: ${rutina.nombre} con ID: $rutinaId")
        } else {
            println("Error al crear la rutina: ${rutina.nombre}")
            throw IllegalStateException("No se pudo crear la rutina: ${rutina.nombre}")
        }
    }

    override fun undo() {
        // Verificar si la rutina se creó correctamente antes de intentar deshacer
        if (rutinaId != null && rutinaId!! > 0) {
            val resultado = mrutina.eliminarRutina(rutinaId!!.toInt())
            if (resultado > 0) {
                println("Rutina eliminada (deshacer): ${rutina.nombre} con ID: $rutinaId")
            } else {
                println("Error al eliminar la rutina durante deshacer: ${rutina.nombre}")
                throw IllegalStateException("No se pudo deshacer la creación de la rutina: ${rutina.nombre}")
            }
        } else {
            println("No hay ninguna rutina creada para deshacer.")
            throw IllegalStateException("No se puede deshacer: la rutina no se creó correctamente.")
        }
    }
}
