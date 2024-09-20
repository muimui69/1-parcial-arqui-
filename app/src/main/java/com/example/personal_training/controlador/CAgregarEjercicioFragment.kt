package com.example.personal_training.controlador

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.personal_training.databinding.VFragmentAgregarEjercicioBinding
import com.example.personal_training.modelo.Ejercicio
import com.example.personal_training.modelo.MEjercicio
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CAgregarEjercicioFragment : Fragment() {

    private var _binding: VFragmentAgregarEjercicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var mejercicio: MEjercicio
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentAgregarEjercicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mejercicio = MEjercicio(requireContext())

        binding.btnSeleccionarImagen.setOnClickListener {
            abrirGaleria()
        }

        binding.btnGuardarEjercicio.setOnClickListener {
            guardarEjercicio()
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data?.data
            binding.tvImagenUrl.text = selectedImageUri.toString()
        }
    }

    private fun guardarEjercicio() {
        val nombre = binding.etNombreEjercicio.text.toString()
        val duracion = binding.etDuracionEjercicio.text.toString()
        val repeticion = binding.etRepeticionEjercicio.text.toString()

        if (nombre.isBlank() || duracion.isBlank() || repeticion.isBlank() || selectedImageUri == null) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        val rutaImagen = copiarImagenAAlmacenamiento(requireContext(), selectedImageUri!!)

        if (rutaImagen == null) {
            Toast.makeText(requireContext(), "Error al guardar la imagen", Toast.LENGTH_LONG).show()
            return
        }

        val nuevoEjercicio = Ejercicio(
            nombre = nombre,
            duracion = duracion,
            repeticion = repeticion,
            imagen_url = rutaImagen
        )

        val resultado = mejercicio.crearEjercicio(nuevoEjercicio)

        if (resultado > 0) {
            Toast.makeText(requireContext(), "Ejercicio guardado con éxito", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Error al guardar el ejercicio", Toast.LENGTH_LONG).show()
        }
    }

    private fun copiarImagenAAlmacenamiento(context: Context, imageUri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

            if (inputStream == null)  return null

            val extension = when (context.contentResolver.getType(imageUri)) {
                "image/png" -> "png"
                "image/jpeg" -> "jpg"
                else -> "jpg"
            }

            val nombreArchivo = "imagen_${System.currentTimeMillis()}.$extension"

            val directorio = File(context.filesDir, "imagenes_ejercicios")
            if (!directorio.exists()) {
                val creado = directorio.mkdir()
                if (!creado) {
                    return null
                }
            }

            val archivo = File(directorio, nombreArchivo)

            val outputStream = FileOutputStream(archivo)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            return archivo.absolutePath

        } catch (e: Exception) {
            return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
