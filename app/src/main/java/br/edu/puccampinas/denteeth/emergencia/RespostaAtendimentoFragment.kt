package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        binding.btncall.setOnClickListener() {


            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefone")
            startActivity(intent)


        }

        binding.btnEnviarLocalizacao.setOnClickListener {
            val intentMaps = Intent(binding.root.context, MapsActivity::class.java)
            this.startActivity(intentMaps)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}