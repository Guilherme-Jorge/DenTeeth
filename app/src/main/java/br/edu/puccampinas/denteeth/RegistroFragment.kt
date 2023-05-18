package br.edu.puccampinas.denteeth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.databinding.FragmentRegistroBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class RegistroFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!
    private val TAG = "DenTeeth Firebase"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmarDados.setOnClickListener {
            (activity as CriarContaActivity).hideSoftKeyboard(binding.btnConfirmarDados)

            if (binding.etNome.text?.isEmpty() == true ||
                binding.etEmail.text?.isEmpty() == true ||
                binding.etSenha.text?.isEmpty() == true ||
                binding.etTelefone.text?.isEmpty() == true ||
                binding.etEndereco1.text?.isEmpty() == true ||
                binding.etCurriculo.text?.isEmpty() == true
            ) {
                Snackbar.make(binding.root, "Há campos obrigatórios vazios.", Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (binding.etCurriculo.text.toString().length > 2000) {
                Snackbar.make(
                    binding.root,
                    "Currículo com mais de 2000 caracteres.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }

            signUpNewAccount(
                binding.etNome.text.toString(),
                binding.etSenha.text.toString(),
                binding.etEmail.text.toString(),
                binding.etTelefone.text.toString(),
                binding.etEndereco1.text.toString(),
                binding.etCurriculo.text.toString(),
                (activity as CriarContaActivity).getFcmToken()
            )
        }
    }

    private fun signUpNewAccount(
        nome: String,
        password: String,
        email: String,
        telefone: String,
        endereco1: String,
        curriculo: String,
        fcmToken: String
    ) {
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    (activity as CriarContaActivity).storeUserId(user!!.uid)

                    updateUserProfile(
                        nome,
                        email,
                        telefone,
                        endereco1,
                        curriculo,
                        user!!.uid,
                        fcmToken
                    )
                        .addOnCompleteListener(requireActivity()) { res ->
                            if (res.result.status == "SUCCESS") {
                                (activity as CriarContaActivity).hideSoftKeyboard(binding.btnConfirmarDados)
                                Snackbar.make(
                                    requireView(),
                                    "Usuário cadastrado com sucesso!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_RegistroFragment_to_TelaCameraFragment)
                            }
                        }
                } else {

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun updateUserProfile(
        nome: String,
        email: String,
        telefone: String,
        endereco1: String,
        curriculo: String,
        uid: String,
        fcmToken: String
    ): Task<CustomResponse> {

        functions = Firebase.functions("southamerica-east1")

        val dadosProfissional = hashMapOf(
            "nome" to nome,
            "telefone" to telefone,
            "email" to email,
            "endereco1" to endereco1,
            "curriculo" to curriculo,
            "uid" to uid,
            "fcmToken" to fcmToken,
            "status" to true
        )

        return functions
            .getHttpsCallable("salvarDadosPessoais")
            .call(dadosProfissional)
            .continueWith { task ->

                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}