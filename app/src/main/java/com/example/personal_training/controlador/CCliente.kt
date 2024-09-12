package com.example.personal_training.controlador

import android.content.Context
import android.util.Log
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.MCliente

class CCliente(context: Context) {
    private val modeloCliente = MCliente(context)

    fun crearCliente(cliente: Cliente): Long {
        return modeloCliente.crearCliente(cliente)
    }

    fun actualizarCliente(cliente: Cliente): Int {
        return modeloCliente.actualizarCliente(cliente)
    }

    fun eliminarCliente(id: Int): Int {
        return modeloCliente.eliminarCliente(id)
    }

    fun obtenerClientes(): List<Cliente> {
        return modeloCliente.obtenerClientes()
    }
}
