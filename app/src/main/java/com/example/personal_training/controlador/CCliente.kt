package com.example.personal_training.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal_training.R
import com.example.personal_training.databinding.VFragmentClienteBinding
import com.example.personal_training.modelo.MCliente
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CCliente : Fragment() {

    private var _binding: VFragmentClienteBinding? = null
    private val binding get() = _binding!!

    private lateinit var mcliente: MCliente
    private lateinit var clienteAdapter: CListaClienteAdapter
    private lateinit var fabVFragmentAgregarClienteBinding: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VFragmentClienteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabVFragmentAgregarClienteBinding = binding.fabVFragmentAgregarCliente

        fabVFragmentAgregarClienteBinding.setOnClickListener {
            findNavController().navigate(R.id.action_nav_clientes_to_agregarClienteFragment)
        }

        mcliente = MCliente(requireContext())
        val listaClientes = mcliente.obtenerClientes().toMutableList()

        clienteAdapter = CListaClienteAdapter(listaClientes,mcliente)

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
