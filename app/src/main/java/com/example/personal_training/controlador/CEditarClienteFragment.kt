package com.example.personal_training.controlador

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.R
import com.example.personal_training.controlador.memento.ClienteCaretaker
import com.example.personal_training.controlador.memento.Originator
import com.example.personal_training.databinding.VFragmentEditarClienteBinding
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.MCliente

class CEditarClienteFragment : Fragment() {

    private var _binding: VFragmentEditarClienteBinding? = null
    private val binding get() = _binding!!

    private var clienteId: Int? = null
    private lateinit var mcliente: MCliente

    private lateinit var originator: Originator
    private val clienteCaretaker = ClienteCaretaker()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEditarClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mcliente = MCliente(requireContext())
        clienteId = arguments?.getInt("clienteId")

        clienteId?.let { id ->
            cargarDatosCliente(id)
        }

        binding.btnEditarCliente.setOnClickListener {
            guardarCambiosCliente()
        }

        binding.btnGuardarTemporales.setOnClickListener {
            guardarCambiosTemporales()
        }

        binding.btnDeshacer.setOnClickListener {
            deshacerCambios()
        }

        binding.btnRehacer.setOnClickListener {
            rehacerCambios()
        }
    }

    private fun cargarDatosCliente(id: Int) {
        val cliente = mcliente.obtenerCliente(id)
        if (cliente != null) {

            // Inicializar el Originator con el estado actual del cliente
            originator = Originator(cliente)

            // Crear un Memento y guardarlo en el ClienteCaretaker
            val memento = originator.createMemento()
            clienteCaretaker.addMemento(memento)

            binding.etNombreCliente.setText(cliente.nombre)
            binding.etPesoCliente.setText(cliente.peso.toString())
            binding.etAlturaCliente.setText(cliente.altura.toString())
            binding.etTelefonoCliente.setText(cliente.telefono)
            binding.etCorreoCliente.setText(cliente.correo)

            val sexoPosition = resources.getStringArray(R.array.sexo_array).indexOf(cliente.sexo)
            binding.spinnerSexo.setSelection(sexoPosition)
        } else {
            Toast.makeText(requireContext(), "Error al cargar cliente", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarCambiosCliente() {
        val nombre = binding.etNombreCliente.text.toString()
        val peso = binding.etPesoCliente.text.toString().toFloatOrNull() ?: 0f
        val altura = binding.etAlturaCliente.text.toString().toFloatOrNull() ?: 0f
        val telefono = binding.etTelefonoCliente.text.toString()
        val correo = binding.etCorreoCliente.text.toString()
        val sexo = binding.spinnerSexo.selectedItem.toString()

        if (nombre.isBlank() || peso <= 0 || altura <= 0 || telefono.isBlank() || correo.isBlank() || sexo.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val clienteEditado = Cliente(
            id = clienteId,
            nombre = nombre,
            peso = peso,
            altura = altura,
            telefono = telefono,
            correo = correo,
            sexo = sexo
        )

        val resultado = mcliente.actualizarCliente(clienteEditado)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Cliente actualizado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()  // Regresar a la pantalla anterior
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el cliente", Toast.LENGTH_LONG).show()
        }
    }

    //memento
    private fun deshacerCambios() {
        val memento = clienteCaretaker.undo()

        Log.d("memento", memento.toString())

        if (memento != null) {
            originator.restore(memento)
            val clienteRestaurado = originator.getState()

            // Restaurar los datos en la interfaz
            binding.etNombreCliente.setText(clienteRestaurado.nombre)
            binding.etPesoCliente.setText(clienteRestaurado.peso.toString())
            binding.etAlturaCliente.setText(clienteRestaurado.altura.toString())
            binding.etTelefonoCliente.setText(clienteRestaurado.telefono)
            binding.etCorreoCliente.setText(clienteRestaurado.correo)

            val sexoPosition = resources.getStringArray(R.array.sexo_array).indexOf(clienteRestaurado.sexo)
            binding.spinnerSexo.setSelection(sexoPosition)
        } else {
            Toast.makeText(requireContext(), "No hay cambios para deshacer", Toast.LENGTH_LONG).show()
        }
    }

    //memento
    private fun rehacerCambios() {
        val memento = clienteCaretaker.redo()
        if (memento != null) {
            originator.restore(memento)
            val clienteRestaurado = originator.getState()

            // Restaurar los datos en la interfaz
            binding.etNombreCliente.setText(clienteRestaurado.nombre)
            binding.etPesoCliente.setText(clienteRestaurado.peso.toString())
            binding.etAlturaCliente.setText(clienteRestaurado.altura.toString())
            binding.etTelefonoCliente.setText(clienteRestaurado.telefono)
            binding.etCorreoCliente.setText(clienteRestaurado.correo)

            val sexoPosition = resources.getStringArray(R.array.sexo_array).indexOf(clienteRestaurado.sexo)
            binding.spinnerSexo.setSelection(sexoPosition)
        } else {
            Toast.makeText(requireContext(), "No hay cambios para rehacer", Toast.LENGTH_LONG).show()
        }
    }

    //memento
    private fun guardarCambiosTemporales() {
        // Crear un nuevo estado del cliente desde los datos del formulario
        val clienteEditado = Cliente(
            id = clienteId,
            nombre = binding.etNombreCliente.text.toString(),
            peso = binding.etPesoCliente.text.toString().toFloatOrNull() ?: 0f,
            altura = binding.etAlturaCliente.text.toString().toFloatOrNull() ?: 0f,
            telefono = binding.etTelefonoCliente.text.toString(),
            correo = binding.etCorreoCliente.text.toString(),
            sexo = binding.spinnerSexo.selectedItem.toString()
        )

        // Actualizar el estado del Originator con el nuevo cliente
        originator = Originator(clienteEditado)

        // Crear un nuevo Memento y guardarlo en el ClienteCaretaker
        val memento = originator.createMemento()
        clienteCaretaker.addMemento(memento)

        Log.d("memento", memento.toString())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
