package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.databinding.VFragmentRutinaBinding
import com.example.personal_training.modelo.MRutina
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CRutina : Fragment() {

    private var _binding: VFragmentRutinaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mrutina: MRutina
    private lateinit var rutinaAdapter: CListaRutinaAdapter
    private lateinit var fabVFragmentAgregarRutinaBinding: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentRutinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabVFragmentAgregarRutinaBinding = binding.fabVFragmentAgregarRutina

        fabVFragmentAgregarRutinaBinding.setOnClickListener {
            findNavController().navigate(R.id.action_nav_rutinas_to_agregarRutinaFragment)
        }

        mrutina = MRutina(requireContext())
        val listaRutinas = mrutina.obtenerRutinas().toMutableList()

        rutinaAdapter = CListaRutinaAdapter(listaRutinas, mrutina)

        binding.recyclerViewRutinas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rutinaAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
