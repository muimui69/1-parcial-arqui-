package com.example.personal_training.controlador

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentEditarEjercicioBinding
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.MEjercicio

class CEditarEjercicioFragment : Fragment() {

    private var _binding: VFragmentEditarEjercicioBinding? = null
    private val binding get() = _binding!!

    private var ejercicioId: Int? = null
    private lateinit var mejercicio: MEjercicio
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentEditarEjercicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mejercicio = MEjercicio(requireContext())
        ejercicioId = arguments?.getInt("ejercicioId")

        ejercicioId?.let { id ->
            cargarDatosEjercicio(id)
        }

        binding.btnEditarEjercicio.setOnClickListener {
            guardarCambiosEjercicio()
        }

        binding.btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }
    }

    private fun cargarDatosEjercicio(id: Int) {
        val ejercicio = mejercicio.obtenerEjercicio(id)
        if (ejercicio != null) {
            binding.etNombreEjercicio.setText(ejercicio.nombre)
            binding.etDuracionEjercicio.setText(ejercicio.duracion)
            binding.etRepeticionEjercicio.setText(ejercicio.repeticion)
            binding.tvImagenUrl.text = ejercicio.imagen_url
        } else {
            Toast.makeText(requireContext(), "Error al cargar ejercicio", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarCambiosEjercicio() {
        val nombre = binding.etNombreEjercicio.text.toString()
        val duracion = binding.etDuracionEjercicio.text.toString()
        val repeticion = binding.etRepeticionEjercicio.text.toString()
        val imagen = binding.tvImagenUrl.text.toString()

        if (nombre.isBlank() || duracion.isBlank() || repeticion.isBlank()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val ejercicioEditado = Ejercicio(
            id = ejercicioId,
            nombre = nombre,
            duracion = duracion,
            repeticion = repeticion,
            imagen_url = imagen
        )

        val resultado = mejercicio.actualizarEjercicio(ejercicioEditado)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Ejercicio actualizado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el ejercicio", Toast.LENGTH_LONG).show()
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            binding.tvImagenUrl.text = selectedImageUri.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
