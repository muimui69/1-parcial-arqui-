package com.example.personal_training.vista.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.R
import com.example.personal_training.controlador.CCliente
import com.example.personal_training.databinding.FragmentAgregarClienteBinding
import com.example.personal_training.modelo.Cliente

class AgregarClienteFragment : Fragment() {

    private var _binding: FragmentAgregarClienteBinding? = null
    private val binding get() = _binding!!
    private lateinit var controladorCliente: CCliente
    private var clienteId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar controlador
        controladorCliente = CCliente(requireContext())
        // Verificar si se han pasado datos de un cliente
        clienteId = arguments?.getInt("clienteId", -1)

        // Configuración del Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sexo_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSexo.adapter = adapter

        val clienteId = arguments?.getInt("clienteId")
        if (clienteId != null) {
            binding.btnGuardarCliente.text = "Actualizar Cliente"
        } else {
            binding.btnGuardarCliente.text = "Guardar Cliente"
        }

        // Si hay un cliente existente, cargar los datos
        clienteId?.let { id ->
            if (id != -1) {
                val cliente = controladorCliente.obtenerCliente(id)
                cliente?.let {
                    // Cargar los datos del cliente en los campos
                    binding.etNombreCliente.setText(cliente.nombre)
                    binding.etPesoCliente.setText(cliente.peso.toString())
                    binding.etAlturaCliente.setText(cliente.altura.toString())
                    binding.etTelefonoCliente.setText(cliente.telefono)
                    binding.etCorreoCliente.setText(cliente.correo)
                    // Seleccionar el sexo en el spinner
                    val sexoPosition = resources.getStringArray(R.array.sexo_array).indexOf(cliente.sexo)
                    binding.spinnerSexo.setSelection(sexoPosition)
                }
            }
        }

        binding.btnGuardarCliente.setOnClickListener {
            val nombre = binding.etNombreCliente.text.toString()
            val peso = binding.etPesoCliente.text.toString().toFloat()
            val altura = binding.etAlturaCliente.text.toString().toFloat()
            val telefono = binding.etTelefonoCliente.text.toString()
            val correo = binding.etCorreoCliente.text.toString()
            val sexo = binding.spinnerSexo.selectedItem.toString()

            if (clienteId == null || clienteId == -1) {
                // Crear un nuevo cliente
                val nuevoCliente = Cliente(
                    nombre = nombre,
                    peso = peso,
                    altura = altura,
                    telefono = telefono,
                    correo = correo,
                    sexo = sexo
                )
                controladorCliente.crearCliente(nuevoCliente)
            } else {
                // Actualizar un cliente existente
                val clienteEditado = Cliente(
                    id = clienteId,
                    nombre = nombre,
                    peso = peso,
                    altura = altura,
                    telefono = telefono,
                    correo = correo,
                    sexo = sexo
                )
                controladorCliente.actualizarCliente(clienteEditado)
            }

            requireActivity().onBackPressed()  // Volver atrás después de guardar
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
