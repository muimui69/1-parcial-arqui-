package com.example.personal_training.controlador.memento

import com.example.personal_training.modelo.Cliente

class Originator(private var cliente: Cliente) {

    fun setState(nuevoEstado: Cliente) {
        cliente = nuevoEstado
    }

    fun getCliente(): Cliente {
        return cliente
    }

    fun createMemento(): Memento {
        return Memento.newMemento(cliente.copy())
    }

    fun restore(memento: Memento) {
        cliente = memento.getState()
        println("Estado restaurado: ${cliente.nombre}, ${cliente.correo}")
    }

}
