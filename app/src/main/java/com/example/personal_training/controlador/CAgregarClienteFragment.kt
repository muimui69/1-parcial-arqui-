package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentAgregarClienteBinding
import com.example.personal_training.modelo.Cliente
import com.example.personal_training.modelo.MCliente

class CAgregarClienteFragment : Fragment() {

    private var _binding: VFragmentAgregarClienteBinding? = null
    private val binding get() = _binding!!

    private lateinit var mcliente: MCliente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentAgregarClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mcliente = MCliente(requireContext())

        binding.btnGuardarCliente.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
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

        val nuevoCliente = Cliente(
            nombre = nombre,
            peso = peso,
            altura = altura,
            telefono = telefono,
            correo = correo,
            sexo = sexo
        )

        val resultado = mcliente.crearCliente(nuevoCliente)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Cliente guardado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al guardar el cliente", Toast.LENGTH_LONG).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
