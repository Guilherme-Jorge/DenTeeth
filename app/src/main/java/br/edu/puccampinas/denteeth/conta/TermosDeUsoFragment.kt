package br.edu.puccampinas.denteeth.conta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.databinding.FragmentTermosDeUsoBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class TermosDeUsoFragment : Fragment() {

    private var _binding: FragmentTermosDeUsoBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTermosDeUsoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val user = auth.currentUser

        binding.btnAceitarTermos.setOnClickListener {
            finishAccountCreation(user!!.uid).addOnCompleteListener(requireActivity()) { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        binding.root,
                        "Usuário criado com sucesso!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    (activity as CriarContaActivity).goToInicio()
                }
            }
        }

        binding.btnRecusarTermos.setOnClickListener {
            Snackbar.make(
                requireView(),
                "É necessário aceitar os termos para criar a conta!",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun finishAccountCreation(uid: String): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        val novosDados = hashMapOf(
            "uid" to uid,
            "criado" to true
        )

        return functions.getHttpsCallable("finalizarCriarConta").call(novosDados)
            .continueWith { task ->
                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}