package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentEditarRutinaBinding
import com.example.personal_training.modelo.MCliente
import com.example.personal_training.modelo.MDieta
import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.Rutina

class CEditarRutinaFragment : Fragment() {

    private var _binding: VFragmentEditarRutinaBinding? = null
    private val binding get() = _binding!!

    private var rutinaId: Int? = null
    private lateinit var mrutina: MRutina
    private lateinit var mcliente: MCliente
    private lateinit var mdieta: MDieta

    private lateinit var clientesIds: List<Int>
    private lateinit var dietasIds: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEditarRutinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mrutina = MRutina(requireContext())
        mcliente = MCliente(requireContext())
        mdieta = MDieta(requireContext())

        rutinaId = arguments?.getInt("rutinaId")

        cargarClientes()
        cargarDietas()

        rutinaId?.let { id ->
            cargarDatosRutina(id)
        }

        binding.btnEditarRutina.setOnClickListener {
            guardarCambiosRutina()
        }
    }

    private fun cargarClientes() {
        val listaClientes = mcliente.obtenerClientes()
        clientesIds = listaClientes.map { it.id!! }

        val clientesNombres = listaClientes.map { it.nombre }
        val adapterClientes = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clientesNombres)
        adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerClientes.adapter = adapterClientes
    }

    private fun cargarDietas() {
        val listaDietas = mdieta.obtenerDietas()
        dietasIds = listaDietas.map { it.id!! }

        val dietasNombres = listaDietas.map { it.titulo }
        val adapterDietas = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dietasNombres)
        adapterDietas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDietas.adapter = adapterDietas
    }

    private fun cargarDatosRutina(id: Int) {
        val rutina = mrutina.obtenerRutina(id)
        if (rutina != null) {
            binding.etNombreRutina.setText(rutina.nombre)
            binding.etTipoRutina.setText(rutina.tipo)

            val clienteIndex = clientesIds.indexOf(rutina.cliente_id)
            if (clienteIndex >= 0) {
                binding.spinnerClientes.setSelection(clienteIndex)
            }

            val dietaIndex = dietasIds.indexOf(rutina.dieta_id)
            if (dietaIndex >= 0) {
                binding.spinnerDietas.setSelection(dietaIndex)
            }

        } else {
            Toast.makeText(requireContext(), "Error al cargar la rutina", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarCambiosRutina() {
        val nombre = binding.etNombreRutina.text.toString()
        val tipo = binding.etTipoRutina.text.toString()
        val clienteId = clientesIds[binding.spinnerClientes.selectedItemPosition]
        val dietaId = dietasIds[binding.spinnerDietas.selectedItemPosition]

        if (nombre.isBlank() || tipo.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val rutinaEditada = Rutina(
            id = rutinaId,
            nombre = nombre,
            tipo = tipo,
            cliente_id = clienteId,
            dieta_id = dietaId
        )

        val resultado = mrutina.actualizarRutina(rutinaEditada)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Rutina actualizada con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar la rutina", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
