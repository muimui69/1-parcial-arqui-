package com.example.personal_training.controlador.memento

import com.example.personal_training.modelo.Cliente

class Memento private constructor(private val state: Cliente) {

    fun getState(): Cliente {
        return state.copy()
    }

    companion object {
        fun newMemento(state: Cliente): Memento {
            return Memento(state)
        }
    }
}
