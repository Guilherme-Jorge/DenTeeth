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
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

class RegistroFragment : Fragment() {

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

            adcionarProfissional()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Usuário salvo com sucesso: ${task.result}")
                        Toast.makeText(
                            binding.root.context,
                            "Usuário salvo com sucesso",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_RegistroFragment_to_TelaCameraFragment)
                    } else {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            Log.e(TAG, "Erro ao salvar usuário: [${e.code}]")
                        } else {
                            Log.w(TAG, "Erro desconhecido: ${task.exception}")
                            Toast.makeText(
                                binding.root.context,
                                "Usuário salvo com sucesso",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_RegistroFragment_to_TelaCameraFragment)
                        }
                    }
                }
        }
    }
    fun dadosProfissional(): HashMap<String, String> {

        return hashMapOf(
            "nome" to binding.etNome.text.toString(),
            "telefone" to binding.etTelefone.text.toString(),
            "email" to binding.etEmail.text.toString(),
            "endereco1" to binding.etEndereco1.text.toString(),
            "curriculo" to binding.etCurriculo.text.toString()
        )
    }

    fun adcionarProfissional(): Task<String> {

        return FirebaseFunctions
            .getInstance("southamerica-east1")
            .getHttpsCallable("salvarDadosPessoais")
            .call(dadosProfissional())
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }
}