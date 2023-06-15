package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.databinding.FragmentRespostaAtendimentoBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RespostaAtendimentoFragment : Fragment() {

    private var _binding: FragmentRespostaAtendimentoBinding? = null

    private val binding get() = _binding!!

    var clicked: Boolean = false

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
            clicked = true
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


    override fun onStart() {
        super.onStart()

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvtemp.setText("" + millisUntilFinished / 1000)
                if(clicked == true){
                    binding.tvtemp.setText("Done!")
                    cancel()
                }
            }

            override fun onFinish() {
                binding.tvtemp.setText("Expirou")

                val intentreturn = Intent(binding.root.context, TelaPrincipalActivity::class.java)
                startActivity(intentreturn)

            }
        }.start()
    }


}