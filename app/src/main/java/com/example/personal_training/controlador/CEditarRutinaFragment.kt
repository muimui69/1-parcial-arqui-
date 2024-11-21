package com.example.personal_training.controlador

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.controlador.command.ActualizarRutinaCommand
import com.example.personal_training.controlador.command.RutinaInvoker
import com.example.personal_training.databinding.VFragmentEditarRutinaBinding
import com.example.personal_training.modelo.*
import com.google.android.material.snackbar.Snackbar

class CEditarRutinaFragment : Fragment() {

    private var _binding: VFragmentEditarRutinaBinding? = null
    private val binding get() = _binding!!

    private var rutinaId: Int? = null
    private lateinit var mrutina: MRutina
    private lateinit var mcliente: MCliente
    private lateinit var mdieta: MDieta
    private lateinit var mejercicio: MEjercicio
    private lateinit var mrutina_ejercicio: MRutinaEjercicio

    private lateinit var clientesIds: List<Int>
    private lateinit var dietasIds: List<Int>

    private lateinit var ejercicioSeleccionAdapter: CListaEjercicioSeleccionAdapter
    private lateinit var listaEjercicios: List<Ejercicio>
    private val ejerciciosSeleccionados = mutableSetOf<Int>()
    private val diasSeleccionados = mutableMapOf<Int, List<String>>()

    private val invoker = RutinaInvoker()

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
        mejercicio = MEjercicio(requireContext())
        mrutina_ejercicio = MRutinaEjercicio(requireContext())

        rutinaId = arguments?.getInt("rutinaId")

        cargarClientes()
        cargarDietas()
        cargarEjercicios()

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

    private fun cargarEjercicios() {
        listaEjercicios = mejercicio.obtenerEjercicios()
        ejercicioSeleccionAdapter = CListaEjercicioSeleccionAdapter(
            listaEjercicios, ejerciciosSeleccionados, diasSeleccionados,
            resources.getStringArray(R.array.dias_rutina_array)
        )

        binding.recyclerViewEjercicios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ejercicioSeleccionAdapter
        }
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

            cargarEjerciciosDeRutina(id)
        } else {
            Toast.makeText(requireContext(), "Error al cargar la rutina", Toast.LENGTH_LONG).show()
        }
    }

    private fun cargarEjerciciosDeRutina(rutinaId: Int) {
        val listaEjerciciosDeRutina = mrutina_ejercicio.obtenerEjerciciosPorRutina(rutinaId)
        ejerciciosSeleccionados.clear()
        diasSeleccionados.clear()
        listaEjerciciosDeRutina.forEach { (ejercicioId, dia) ->
            ejercicioId?.let {
                ejerciciosSeleccionados.add(it)
                val diasExistentes = diasSeleccionados[it]?.toMutableList() ?: mutableListOf()
                diasExistentes.add(dia)
                diasSeleccionados[it] = diasExistentes
            }
        }
        ejercicioSeleccionAdapter.notifyDataSetChanged()
    }

    private fun guardarCambiosRutina() {
        if (!isAdded) {
            Log.e("CEditarRutinaFragment", "El fragmento no está adjunto al contexto.")
            return
        }

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

//        val resultado = mrutina.actualizarRutina(rutinaEditada)
//
//        if (resultado > 0) {
//            mrutina_ejercicio.eliminarEjerciciosPorRutina(rutinaId!!)
//
//            for (ejercicioId in ejerciciosSeleccionados) {
//                val dias = diasSeleccionados[ejercicioId]
//                if (dias != null) {
//                    for (dia in dias) {
//                        val rutinaEjercicio = RutinaEjercicio(
//                            dia_rutina = dia,
//                            rutina_id = rutinaId!!,
//                            ejercicio_id = ejercicioId
//                        )
//                        mrutina_ejercicio.asociarEjercicioARutina(rutinaEjercicio)
//                    }
//                }
//            }
//
//            Toast.makeText(requireContext(), "Rutina actualizada con éxito", Toast.LENGTH_LONG).show()
//
//            requireActivity().onBackPressed()
//        } else {
//            Toast.makeText(requireContext(), "Error al actualizar la rutina", Toast.LENGTH_LONG).show()
//        }

        // Ejecutar el comando de actualización
        val actualizarCommand = ActualizarRutinaCommand(mrutina, rutinaEditada)
        invoker.setCommand(actualizarCommand)
        invoker.executeCommand()

        // Actualizar la relación de ejercicios en la base de datos
        mrutina_ejercicio.eliminarEjerciciosPorRutina(rutinaId!!)
        for (ejercicioId in ejerciciosSeleccionados) {
            val dias = diasSeleccionados[ejercicioId]
            if (dias != null) {
                for (dia in dias) {
                    val rutinaEjercicio = RutinaEjercicio(
                        dia_rutina = dia,
                        rutina_id = rutinaId!!,
                        ejercicio_id = ejercicioId
                    )
                    mrutina_ejercicio.asociarEjercicioARutina(rutinaEjercicio)
                }
            }
        }

        // Toast.makeText(requireContext(), "Rutina actualizada con éxito", Toast.LENGTH_LONG).show()
        //requireActivity().onBackPressed()

        // Mostrar el Snackbar con la opción de deshacer
        if (isAdded) {
            Snackbar.make(binding.root, "Cambios guardados con éxito", Snackbar.LENGTH_LONG)
                .setAction("Deshacer") {
                    deshacerCambios()
                }
                .show()
        }
    }

    private fun deshacerCambios() {
        if (!isAdded) {
            Log.e("CEditarRutinaFragment", "El fragmento no está adjunto al contexto.")
            return
        }

        try {
            invoker.undoCommand()
            if (isAdded) { // Verificación antes de interactuar con la UI
                Toast.makeText(requireContext(), "Cambios deshechos", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed() // Regresar a la vista anterior
            }
        } catch (e: IllegalStateException) {
            Log.e("CEditarRutinaFragment", "Error al deshacer los cambios", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
