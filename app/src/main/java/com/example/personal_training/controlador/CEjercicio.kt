package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.databinding.VFragmentEjercicioBinding
import com.example.personal_training.modelo.MEjercicio
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CEjercicio : Fragment() {

    private var _binding: VFragmentEjercicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var ejercicioAdapter: CListaEjercicioAdapter
    private lateinit var fabVFragmentAgregarEjercicioBinding: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEjercicioBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabVFragmentAgregarEjercicioBinding = binding.fabVFragmentAgregarEjercicio

        fabVFragmentAgregarEjercicioBinding.setOnClickListener {
            findNavController().navigate(R.id.action_nav_ejercicios_to_agregarEjercicioFragment)
        }

        val modeloEjercicio = MEjercicio(requireContext())
        val listaEjercicios = modeloEjercicio.obtenerEjercicios().toMutableList()

        ejercicioAdapter = CListaEjercicioAdapter(listaEjercicios,modeloEjercicio)

        binding.recyclerViewEjercicios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ejercicioAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
