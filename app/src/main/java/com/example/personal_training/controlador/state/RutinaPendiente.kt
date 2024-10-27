package com.example.personal_training.controlador.state

import EnvioRutinaContext

class RutinaPendiente(private val context: EnvioRutinaContext) : State {

    override fun sendRutine() {
        println("Enviando rutina...")
        context.cambiarEstado(RutinaEnviada(context))
    }

    override fun cancelShipment() {
        println("El envío ha sido cancelado.")
        context.cambiarEstado(RutinaCancelada(context))
    }
}
