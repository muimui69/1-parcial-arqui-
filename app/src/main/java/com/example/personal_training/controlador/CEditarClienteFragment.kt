package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.R
import com.example.personal_training.databinding.VFragmentEditarClienteBinding
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.MCliente

class CEditarClienteFragment : Fragment() {

    private var _binding: VFragmentEditarClienteBinding? = null
    private val binding get() = _binding!!

    private var clienteId: Int? = null
    private lateinit var mcliente: MCliente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEditarClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el modelo
        mcliente = MCliente(requireContext())

        // Obtenemos el ID del cliente desde los argumentos
        clienteId = arguments?.getInt("clienteId")

        // Si tenemos un ID, cargamos los datos del cliente
        clienteId?.let { id ->
            cargarDatosCliente(id)
        }

        // Configuramos el botón de guardar
        binding.btnEditarCliente.setOnClickListener {
            guardarCambiosCliente()
        }
    }

    private fun cargarDatosCliente(id: Int) {
        val cliente = mcliente.obtenerCliente(id)
        if (cliente != null) {
            binding.etNombreCliente.setText(cliente.nombre)
            binding.etPesoCliente.setText(cliente.peso.toString())
            binding.etAlturaCliente.setText(cliente.altura.toString())
            binding.etTelefonoCliente.setText(cliente.telefono)
            binding.etCorreoCliente.setText(cliente.correo)

            // Configurar el valor en el spinner para el sexo
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

        // Validaciones de campos vacíos
        if (nombre.isBlank() || peso <= 0 || altura <= 0 || telefono.isBlank() || correo.isBlank() || sexo.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val clienteEditado = Cliente(
            id = clienteId,  // Mantener el ID del cliente
            nombre = nombre,
            peso = peso,
            altura = altura,
            telefono = telefono,
            correo = correo,
            sexo = sexo
        )

        // Actualizar cliente en la base de datos
        val resultado = mcliente.actualizarCliente(clienteEditado)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Cliente actualizado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()  // Regresar a la pantalla anterior
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el cliente", Toast.LENGTH_LONG).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
