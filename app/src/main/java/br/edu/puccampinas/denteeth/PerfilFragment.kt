package br.edu.puccampinas.denteeth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.puccampinas.denteeth.databinding.FragmentPerfilBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEditar.setOnClickListener {
            (activity as TelaPrincipalActivity).goToEditarPerfil()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}