package com.example.personal_training.controlador.command

class RutinaInvoker {
    private var command: Command? = null

    fun setCommand(cmd: Command) {
        this.command = cmd
    }

    fun executeCommand() {
        command?.execute() ?: println("No hay comando configurado.")
    }
}
