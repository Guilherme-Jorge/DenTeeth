package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.CustomResponse
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.databinding.FragmentEmergenciaBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EmergenciaFragment : Fragment() {

    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentEmergenciaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEmergenciaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ((activity as AtenderEmergenciaActivity).intent.hasExtra("descricao")) {
            val descricao = (activity as AtenderEmergenciaActivity).intent.getStringExtra("descricao")
            binding.tvMotivoEmergencia.text = descricao

            Glide.with(this)
                .load((activity as AtenderEmergenciaActivity).intent.getStringExtra("fotos1"))
                .into(binding.ivEmergenciaFotos)
        }

        binding.btnAceitar.setOnClickListener {
            responderChamado(true).addOnCompleteListener(requireActivity()) { res ->
                if (res.result.status == "SUCCESS") {
                    findNavController().navigate(R.id.action_EmergenciaFragment_to_AguardandoRespostaFragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Ocorreu um erro, tente novamente!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.btnNegar.setOnClickListener {
            responderChamado(false).addOnCompleteListener(requireActivity()) { res ->
                if (res.result.status == "SUCCESS") {
                    val intentTelaPrincipal = Intent(binding.root.context, TelaPrincipalActivity::class.java)

                    this.startActivity(intentTelaPrincipal)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Ocorreu um erro, tente novamente!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.btnImagem1.setOnClickListener {
            Glide.with(this)
                .load((activity as AtenderEmergenciaActivity).intent.getStringExtra("fotos1"))
                .into(binding.ivEmergenciaFotos)

            binding.btnImagem1.setTextColor(resources.getColor(R.color.gray))
            binding.btnImagem2.setTextColor(resources.getColor(R.color.baby_blue_500))
            binding.btnImagem3.setTextColor(resources.getColor(R.color.baby_blue_500))

            binding.btnImagem1.isClickable = false
            binding.btnImagem2.isClickable = true
            binding.btnImagem3.isClickable = true
        }

        binding.btnImagem2.setOnClickListener {
            Glide.with(this)
                .load((activity as AtenderEmergenciaActivity).intent.getStringExtra("fotos2"))
                .into(binding.ivEmergenciaFotos)

            binding.btnImagem1.setTextColor(resources.getColor(R.color.baby_blue_500))
            binding.btnImagem2.setTextColor(resources.getColor(R.color.gray))
            binding.btnImagem3.setTextColor(resources.getColor(R.color.baby_blue_500))

            binding.btnImagem1.isClickable = true
            binding.btnImagem2.isClickable = false
            binding.btnImagem3.isClickable = true
        }

        binding.btnImagem3.setOnClickListener {
            Glide.with(this)
                .load((activity as AtenderEmergenciaActivity).intent.getStringExtra("fotos3"))
                .into(binding.ivEmergenciaFotos)

            binding.btnImagem1.setTextColor(resources.getColor(R.color.baby_blue_500))
            binding.btnImagem2.setTextColor(resources.getColor(R.color.baby_blue_500))
            binding.btnImagem3.setTextColor(resources.getColor(R.color.gray))

            binding.btnImagem1.isClickable = true
            binding.btnImagem2.isClickable = true
            binding.btnImagem3.isClickable = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun responderChamado(check: Boolean): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")
        auth = Firebase.auth

        var status = "ACEITA"

        if (!check) {
            status = "REJEITADA"
        }

        val dadosChamado = hashMapOf(
            "profissional" to auth.currentUser!!.uid,
            "emergencia" to (activity as AtenderEmergenciaActivity).intent.getStringExtra("id"),
            "status" to status
        )

        return functions
            .getHttpsCallable("responderChamado")
            .call(dadosChamado)
            .continueWith { task ->

                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}