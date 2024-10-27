package com.example.personal_training.controlador.state

import EnvioRutinaContext

class RutinaCancelada(private val context: EnvioRutinaContext) : State {
    override fun sendRutine() {
        println("No se puede enviar, la rutina fue cancelada.")
    }

    override fun cancelShipment() {
        println("La rutina ya está cancelada.")
    }
}
