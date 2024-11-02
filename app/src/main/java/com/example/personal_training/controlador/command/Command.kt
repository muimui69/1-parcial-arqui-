package com.example.personal_training.controlador.command

interface Command {
    fun execute()
    fun undo()
}

