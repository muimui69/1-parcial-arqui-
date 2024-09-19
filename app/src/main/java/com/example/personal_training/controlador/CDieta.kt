package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.databinding.VFragmentDietaBinding
import com.example.personal_training.modelo.MDieta
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CDieta : Fragment() {

    private var _binding: VFragmentDietaBinding? = null
    private val binding get() = _binding!!

    private lateinit var dietaAdapter: CListaDietaAdapter
    private lateinit var fabVFragmentAgregarDietaBinding: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentDietaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabVFragmentAgregarDietaBinding = binding.fabVFragmentAgregarDieta

        fabVFragmentAgregarDietaBinding.setOnClickListener {
            findNavController().navigate(R.id.action_nav_dietas_to_agregarDietaFragment)
        }

        val modeloDieta = MDieta(requireContext())
        val listaDietas = modeloDieta.obtenerDietas().toMutableList()

        dietaAdapter = CListaDietaAdapter(listaDietas,modeloDieta)

        binding.recyclerViewDietas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dietaAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}