package com.example.personal_training.controlador.command

import android.util.Log
import java.util.Stack

class RutinaInvoker {
    private var command: Command? = null
    private val commandHistory = Stack<Command>()

    fun setCommand(command: Command) {
        this.command = command
    }

    fun executeCommand() {
        command?.let {
            it.execute()
            commandHistory.push(it)
        } ?: Log.e("RutinaInvoker", "No se ha configurado un comando para ejecutar.")
    }

    fun undoCommand() {
        if (commandHistory.isNotEmpty()) {
            val lastCommand = commandHistory.pop()
            lastCommand.undo()
            Log.d("RutinaInvoker", "Comando deshecho.")
        }else {
            Log.w("RutinaInvoker", "No hay comandos en el historial para deshacer.")
        }
    }
}
