package br.edu.puccampinas.denteeth.emergencia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.databinding.FragmentAguardandoRespostaBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AguardandoRespostaFragment : Fragment() {

    private var _binding: FragmentAguardandoRespostaBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAguardandoRespostaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTemp.setOnClickListener {
            findNavController().navigate(R.id.action_AguardandoRespostaFragment_to_RespostaAtendimentoFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}