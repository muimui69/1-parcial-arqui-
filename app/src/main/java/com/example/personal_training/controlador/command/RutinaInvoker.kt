package com.example.personal_training.controlador.command

import java.util.Stack

class RutinaInvoker {
    private var command: Command? = null
    private val commandHistory = Stack<Command>()

    fun setCommand(command: Command) {
        this.command = command
    }

    fun executeCommand() {
        command?.execute()
        command?.let { commandHistory.push(it) }
    }

    fun undoCommand() {
        if (commandHistory.isNotEmpty()) {
            val lastCommand = commandHistory.pop()
            lastCommand.undo()
        }
    }
}
