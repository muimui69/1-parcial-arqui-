package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.databinding.VFragmentEnviarRutinaBinding
import com.example.personal_training.modelo.MCliente
import com.example.personal_training.modelo.MDieta
import com.example.personal_training.modelo.MEjercicio
import com.example.personal_training.modelo.MRutina

class CEnviarRutina : Fragment() {

    private var _binding: VFragmentEnviarRutinaBinding? = null
    private val binding get() = _binding!!

    private lateinit var rutinaAdapter: CListaEnviarRutinaAdapter
    private lateinit var mcliente: MCliente
    private lateinit var mrutina: MRutina
    private lateinit var mdieta: MDieta
    private lateinit var mejercicio: MEjercicio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEnviarRutinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mrutina = MRutina(requireContext())
        mcliente = MCliente(requireContext())
        mdieta = MDieta(requireContext())
        mejercicio = MEjercicio(requireContext())


        val listaClientes = mcliente.obtenerClientes().toMutableList()

        val clientesMap = listaClientes.associateBy { it.id!! }
        val listaDietasMap = mdieta.obtenerDietas().toMutableList().associateBy { it.id!! }
        val listaEjerciciosMap = mejercicio.obtenerEjercicios().toMutableList().associateBy { it.id!! }

        val clientesIds = listaClientes.map { it.id }
        val listaRutinasConClientes = mrutina.obtenerRutinasConClientes(clientesIds)

        rutinaAdapter = CListaEnviarRutinaAdapter(listaRutinasConClientes, clientesMap, listaDietasMap, listaEjerciciosMap)

        binding.recyclerViewEnviarRutina.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rutinaAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

