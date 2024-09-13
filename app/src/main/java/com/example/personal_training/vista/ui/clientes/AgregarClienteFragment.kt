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

        // Configuración del Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sexo_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSexo.adapter = adapter


        // Configuración del botón para guardar cliente
        binding.btnGuardarCliente.setOnClickListener {
            val nombre = binding.etNombreCliente.text.toString()
            val peso = binding.etPesoCliente.text.toString()
            val altura = binding.etAlturaCliente.text.toString()
            val telefono = binding.etTelefonoCliente.text.toString()
            val correo = binding.etCorreoCliente.text.toString()
            val sexo = binding.spinnerSexo.selectedItem.toString()

            // Validar los datos ingresados
            if (nombre.isEmpty() || peso.isEmpty() || altura.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Crear nuevo cliente
                val nuevoCliente = Cliente(
                    nombre = nombre,
                    peso = peso.toFloat(),
                    altura = altura.toFloat(),
                    telefono = telefono,
                    correo = correo,
                    sexo = sexo
                )

                val result = controladorCliente.crearCliente(nuevoCliente)

                if (result > 0) {
                    Toast.makeText(requireContext(), "Cliente registrado con éxito", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Error al registrar el cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
