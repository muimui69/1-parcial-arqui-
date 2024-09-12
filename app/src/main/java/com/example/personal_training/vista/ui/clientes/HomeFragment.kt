package com.example.personal_training.vista.ui.clientes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.controlador.CCliente
import com.example.personal_training.databinding.FragmentClientesBinding
import com.example.personal_training.modelo.Cliente

class HomeFragment : Fragment() {

    private var _binding: FragmentClientesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controladorCliente = CCliente(requireContext())
        val listaClientes: List<Cliente> = controladorCliente.obtenerClientes()

        if (listaClientes.isEmpty()) {
            binding.recyclerViewClientes.visibility = View.GONE
            binding.textNoClientes.visibility = View.VISIBLE
        } else {
            binding.recyclerViewClientes.visibility = View.VISIBLE
            binding.textNoClientes.visibility = View.GONE
            binding.recyclerViewClientes.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ClienteAdapter(listaClientes)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}