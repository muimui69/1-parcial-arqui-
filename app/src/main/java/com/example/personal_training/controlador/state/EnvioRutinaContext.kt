import com.example.personal_training.controlador.state.State

class EnvioRutinaContext(estadoInicial: State) {
    private var estado: State = estadoInicial

    fun cambiarEstado(nuevoEstado: State) {
        estado = nuevoEstado
        println("Estado cambiado a: ${estado::class.simpleName}")
    }

    fun enviarRutina() {
        estado.sendRutine()
    }

    fun cancelarEnvio() {
        estado.cancelShipment()
    }
}
