package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentAgregarDietaBinding
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.MDieta

class CAgregarDietaFragment : Fragment() {

    private var _binding: VFragmentAgregarDietaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mdieta: MDieta

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentAgregarDietaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mdieta = MDieta(requireContext())

        binding.btnGuardarDieta.setOnClickListener {
            guardarDieta()
        }
    }

    private fun guardarDieta() {
        val titulo = binding.etTituloDieta.text.toString()
        val descripcion = binding.etDescripcionDieta.text.toString()

        if (titulo.isBlank() || descripcion.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val nuevaDieta = Dieta(
            titulo = titulo,
            descripcion = descripcion
        )

        val resultado = mdieta.crearDieta(nuevaDieta)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Dieta guardada con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al guardar la dieta", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
