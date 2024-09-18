package com.example.personal_training.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.controlador.CListaClienteAdapter
import com.example.personal_training.databinding.VFragmentClienteBinding
import com.example.personal_training.modelo.MCliente


class VCliente : Fragment() {

    private var _binding: VFragmentClienteBinding? = null
    private val binding get() = _binding!!

    private lateinit var clienteAdapter: CListaClienteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentClienteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modeloCliente = MCliente(requireContext())
        val listaClientes = modeloCliente.obtenerClientes().toMutableList()

        clienteAdapter = CListaClienteAdapter(listaClientes,modeloCliente)

        binding.recyclerViewClientes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = clienteAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}