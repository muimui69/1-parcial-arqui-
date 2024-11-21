package com.example.personal_training.controlador.command

import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class EliminarRutinaCommand(
    private val mrutina: MRutina,
    private val rutinaId: Int,
) : Command {

    private var backup: Rutina? = null

    override fun execute() {
        // Respaldar la rutina antes de eliminarla
        backup = mrutina.obtenerRutina(rutinaId)
        if (backup != null) {
            val resultado = mrutina.eliminarRutina(rutinaId)
            if (resultado > 0) {
                println("Rutina eliminada: ${backup?.nombre} con ID: $rutinaId")
            } else {
                println("Error al eliminar la rutina con ID: $rutinaId")
                throw IllegalStateException("No se pudo eliminar la rutina con ID: $rutinaId")
            }
        } else {
            println("No se encontró la rutina con ID: $rutinaId para eliminar.")
            throw IllegalStateException("La rutina con ID: $rutinaId no existe.")
        }
    }

    override fun undo() {
        if (backup != null) {
            val resultado = mrutina.crearRutina(backup!!)
            if (resultado > 0) {
                println("Rutina restaurada: ${backup?.nombre} con ID: ${backup?.id}")
            } else {
                println("Error al restaurar la rutina: ${backup?.nombre}")
                throw IllegalStateException("No se pudo restaurar la rutina: ${backup?.nombre}")
            }
        } else {
            println("No se puede deshacer: no hay respaldo disponible.")
            throw IllegalStateException("No hay respaldo para restaurar la rutina eliminada.")
        }
    }
}
