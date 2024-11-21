package com.example.personal_training.controlador.command

import android.content.Context
import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.MRutinaEjercicio
import com.example.personal_training.modelo.Rutina
import com.example.personal_training.modelo.RutinaEjercicio

class EliminarRutinaCommand(
    private val mrutina: MRutina,
    private val rutinaId: Int,
    private val context: Context
) : Command {

    private var backup: Rutina? = null
    private var backupEjercicios: List<Pair<Int?, String>>? = null
    private val mrutinaEjercicio: MRutinaEjercicio = MRutinaEjercicio(context)

    override fun execute() {
        // Respaldar la rutina antes de eliminarla
        backup = mrutina.obtenerRutina(rutinaId)
        backupEjercicios = mrutinaEjercicio.obtenerEjerciciosPorRutina(rutinaId)

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
        backup?.let {
            // Restaurar la rutina en la base de datos
            val rutinaIdRestaurado = mrutina.crearRutina(it)
            if (rutinaIdRestaurado > 0) {
                // Restaurar los ejercicios asociados a la rutina
                backupEjercicios?.forEach { (ejercicioId, dia) ->
                    if (ejercicioId != null) {
                        mrutinaEjercicio.asociarEjercicioARutina(
                            RutinaEjercicio(
                                rutina_id = rutinaIdRestaurado.toInt(),
                                ejercicio_id = ejercicioId,
                                dia_rutina = dia
                            )
                        )
                    }
                }
            } else {
                throw IllegalStateException("No se pudo restaurar la rutina.")
            }
        } ?: throw IllegalStateException("No hay datos respaldados para restaurar.")
    }

}
