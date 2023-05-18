package br.edu.puccampinas.denteeth.emergencia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.puccampinas.denteeth.CriarContaActivity
import br.edu.puccampinas.denteeth.CustomResponse
import br.edu.puccampinas.denteeth.databinding.FragmentEmergenciaBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
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
                .load((activity as AtenderEmergenciaActivity).intent.getStringExtra("fotos"))
                .into(binding.ivEmergenciaFoto)
        }

        binding.btnAceitar.setOnClickListener {
            responderChamado(true).addOnCompleteListener(requireActivity()) { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        requireView(),
                        "Chamado respondido com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.btnNegar.setOnClickListener {
            responderChamado(false).addOnCompleteListener(requireActivity()) { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        requireView(),
                        "Chamado respondido com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun responderChamado(check: Boolean): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        var status = "ACEITA"

        if (!check) {
            status = "REJEITADA"
        }

        val dadosChamado = hashMapOf(
            // TODO: Adcionar UID do usuario
            "profissional" to "user",
            "emergencia" to (activity as AtenderEmergenciaActivity).intent.hasExtra("id"),
            "status" to status,
            "dataHora" to "05/05/2023"
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