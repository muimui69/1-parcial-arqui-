package com.example.personal_training.controlador.memento

import com.example.personal_training.modelo.Cliente

class Originator(private var estado: Cliente) {

    fun write(nuevoEstado: Cliente) {
        estado = nuevoEstado
    }

    fun getState(): Cliente {
        return estado
    }

    fun createMemento(): Memento {
        return Memento.newMemento(estado.copy())
    }

    fun restore(memento: Memento) {
        estado = memento.getState()
        println("Estado restaurado: ${estado.nombre}, ${estado.correo}")
    }

    fun setMemento(memento: Memento) {
        estado = memento.getState()
        println("Estado establecido desde el memento: ${estado.nombre}, ${estado.correo}")
    }
}
