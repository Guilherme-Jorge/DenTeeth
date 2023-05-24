package br.edu.puccampinas.denteeth.emergencia

import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.puccampinas.denteeth.databinding.FragmentRespostaAtendimentoBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RespostaAtendimentoFragment : Fragment() {

    private var _binding: FragmentRespostaAtendimentoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRespostaAtendimentoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val telefone = (activity as AtenderEmergenciaActivity).intent.getStringExtra("telefone")
        binding.tvNumeroTelefone.text = telefone

        binding.btnCopy.setOnClickListener {
            (activity as AtenderEmergenciaActivity).copyToClipboard(telefone)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}