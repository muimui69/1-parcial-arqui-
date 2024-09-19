package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentEditarDietaBinding
import com.example.personal_training.modelo.Dieta
import com.example.personal_training.modelo.MDieta

class CEditarDietaFragment : Fragment() {

    private var _binding: VFragmentEditarDietaBinding? = null
    private val binding get() = _binding!!

    private var dietaId: Int? = null
    private lateinit var mdieta: MDieta

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEditarDietaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mdieta = MDieta(requireContext())
        dietaId = arguments?.getInt("dietaId")

        dietaId?.let { id ->
            cargarDatosDieta(id)
        }

        binding.btnEditarDieta.setOnClickListener {
            guardarCambiosDieta()
        }
    }

    private fun cargarDatosDieta(id: Int) {
        val dieta = mdieta.obtenerDieta(id)
        if (dieta != null) {
            binding.etDescripcionDieta.setText(dieta.descripcion)
        } else {
            Toast.makeText(requireContext(), "Error al cargar dieta", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarCambiosDieta() {
        val descripcion = binding.etDescripcionDieta.text.toString()

        if (descripcion.isBlank() ) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }
        val dietaEditada = Dieta(
            id = dietaId,
            descripcion =descripcion
        )

        val resultado = mdieta.actualizarDieta(dietaEditada)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Dieta actualizado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el dieta", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
