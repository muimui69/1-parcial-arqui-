package com.example.personal_training.controlador.memento

class ClienteCaretaker {
    private val history = mutableListOf<Memento>()
    private var pos = -1

    fun addMemento(memento: Memento) {
        if (pos < history.size - 1) {
            history.subList(pos + 1, history.size).clear()
        }
        history.add(memento)
        pos++
    }

    fun undo(): Memento? {
        return pos.takeIf { it > 0 }
            ?.let {
                pos--
                history[pos]
            }
    }

    fun redo(): Memento? {
        return (pos + 1).takeIf { it < history.size }
            ?.let {
                pos++
                history[pos]
            }
    }
}
