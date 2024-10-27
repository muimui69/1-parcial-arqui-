package com.example.personal_training.controlador.state

import EnvioRutinaContext

class RutinaEnviada(private val context: EnvioRutinaContext) : State {
    override fun sendRutine() {
        println("La rutina ya ha sido enviada.")
    }

    override fun cancelShipment() {
        println("No se puede cancelar, la rutina ya fue enviada.")
    }
}
