package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.controlador.command.CrearRutinaCommand
import com.example.personal_training.controlador.command.RutinaInvoker
import com.example.personal_training.databinding.VFragmentAgregarRutinaBinding
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.MCliente
import com.example.personal_training.modelo.MDieta
import com.example.personal_training.modelo.MEjercicio
import com.example.personal_training.modelo.MRutina
import com.example.personal_training.modelo.MRutinaEjercicio
import com.example.personal_training.modelo.Rutina
import com.example.personal_training.modelo.RutinaEjercicio

class CAgregarRutinaFragment : Fragment() {

    private var _binding: VFragmentAgregarRutinaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mrutina: MRutina
    private lateinit var mcliente: MCliente
    private lateinit var mdieta: MDieta
    private lateinit var mejercicio: MEjercicio
    private lateinit var mrutina_ejercicio: MRutinaEjercicio

    private lateinit var clientesIds: MutableList<Int>
    private lateinit var dietasIds: MutableList<Int>
    
    private lateinit var ejercicioSeleccionAdapter: CListaEjercicioSeleccionAdapter
    private lateinit var listaEjercicios: List<Ejercicio>
    private val ejerciciosSeleccionados = mutableSetOf<Int>()
    private val diasSeleccionados = mutableMapOf<Int, List<String>>()

    private val invoker = RutinaInvoker()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentAgregarRutinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mrutina = MRutina(requireContext())
        mcliente = MCliente(requireContext())
        mdieta = MDieta(requireContext())
        mejercicio = MEjercicio(requireContext())
        mrutina_ejercicio = MRutinaEjercicio(requireContext())

        cargarClientes()
        cargarDietas()
        cargarEjercicios()

        binding.btnGuardarRutina.setOnClickListener {
            guardarRutina()
        }
    }

    private fun cargarClientes() {
        val listaClientes = mcliente.obtenerClientes()
        val clientesNombres = mutableListOf("Selecciona un cliente")
        clientesNombres.addAll(listaClientes.map { it.nombre })

        clientesIds = mutableListOf(-1)
        clientesIds.addAll(listaClientes.map { it.id!! })

        val adapterClientes = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clientesNombres)
        adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerClientes.adapter = adapterClientes
    }

    private fun cargarDietas() {
        val listaDietas = mdieta.obtenerDietas()
        val dietasTitulos = mutableListOf("Selecciona una dieta")
        dietasTitulos.addAll(listaDietas.map { it.titulo })

        dietasIds = mutableListOf(-1)
        dietasIds.addAll(listaDietas.map { it.id!! })

        val adapterDietas = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dietasTitulos)
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

    private fun guardarRutina() {
        val nombre = binding.etNombreRutina.text.toString()
        val tipo = binding.etTipoRutina.text.toString()

        val clientePos = binding.spinnerClientes.selectedItemPosition
        val dietaPos = binding.spinnerDietas.selectedItemPosition

        val clienteId = clientesIds[clientePos]
        val dietaId = dietasIds[dietaPos]

        if (clienteId == -1 || dietaId == -1) {
            Toast.makeText(requireContext(), "Por favor selecciona un cliente y una dieta", Toast.LENGTH_LONG).show()
            return
        }

        if (nombre.isBlank() || tipo.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val rutinaCreada = Rutina(
            nombre = nombre,
            tipo = tipo,
            cliente_id = clienteId,
            dieta_id = dietaId
        )

//        val resultado = mrutina.crearRutina(rutinaCreada)
//
//        if (resultado > 0) {
//
//            val rutinaId = resultado.toInt()
//
//            for (ejercicioId in ejerciciosSeleccionados) {
//                val dias = diasSeleccionados[ejercicioId]
//                if (dias != null) {
//                    for (dia in dias) {
//                        val rutinaEjercicio = RutinaEjercicio (
//                            dia_rutina = dia,
//                            rutina_id = rutinaId,
//                            ejercicio_id = ejercicioId
//                        )
//                        mrutina_ejercicio.asociarEjercicioARutina(rutinaEjercicio)
//                    }
//                }
//            }
//
//            Toast.makeText(requireContext(), "Rutina guardada con éxito", Toast.LENGTH_LONG).show()
//            requireActivity().onBackPressed()
//        } else {
//            Toast.makeText(requireContext(), "Error al guardar la rutina", Toast.LENGTH_LONG).show()
//        }

        // Crear el comando de creación y ejecutarlo mediante el invoker
        val crearCommand = CrearRutinaCommand(mrutina, rutinaCreada)
        invoker.setCommand(crearCommand)
        invoker.executeCommand()

        // Obtener el ID de la rutina recién creada para asociar los ejercicios
        val rutinaId = crearCommand.rutinaId?.toInt() ?: return

        // Asocia los ejercicios seleccionados a la rutina creada
        for (ejercicioId in ejerciciosSeleccionados) {
            val dias = diasSeleccionados[ejercicioId]
            if (dias != null) {
                for (dia in dias) {
                    val rutinaEjercicio = RutinaEjercicio(
                        dia_rutina = dia,
                        rutina_id = rutinaId,
                        ejercicio_id = ejercicioId
                    )
                    mrutina_ejercicio.asociarEjercicioARutina(rutinaEjercicio)
                }
            }
        }

        Toast.makeText(requireContext(), "Rutina guardada con éxito", Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
