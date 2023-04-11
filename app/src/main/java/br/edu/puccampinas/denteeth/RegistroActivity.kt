package br.edu.puccampinas.denteeth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.edu.puccampinas.denteeth.databinding.ActivityRegistroBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val TAG = "DenTeeth Firebase"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirmarDados.setOnClickListener {
            hideSoftKeyboard(binding.btnConfirmarDados)


                adcionarProfissional()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Usuário salvo com sucesso: ${task.result}")
                        } else {
                            val e = task.exception
                            if (e is FirebaseFunctionsException) {
                                Log.e(TAG, "Erro ao salvar usuário: [${e.code}] ${e.details}")
                            } else {
                                Log.w(TAG, "Erro desconhecido: ${task.exception}")
                            }
                        }
                    }


            val intentTelaCamera = Intent(this, TelaCameraActivity::class.java)

//            val dadosProfissional = dadosProfissional()

//            intentTelaCamera.putExtra("profissional", dadosProfissional)

            this.startActivity(intentTelaCamera)
        }
    }

    private fun hideSoftKeyboard(v: View) {

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun dadosProfissional(): HashMap<String, String> {
        return hashMapOf(
            "nome" to binding.etNome.text.toString(),
            "telefone" to binding.etTelefone.text.toString(),
            "email" to binding.etEmail.text.toString(),
            "endereco1" to binding.etEndereco1.text.toString(),
            "curriculo" to binding.etCurriculo.text.toString()
        )
    }

    private fun adcionarProfissional(): Task<String> {
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