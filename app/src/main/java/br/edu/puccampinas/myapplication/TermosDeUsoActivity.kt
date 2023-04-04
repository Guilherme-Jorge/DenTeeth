package br.edu.puccampinas.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.edu.puccampinas.myapplication.databinding.ActivityTermosdeusoBinding
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.android.gms.tasks.Task

class TermosDeUsoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermosdeusoBinding
    private val TAG = "MainActivity Firebase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermosdeusoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAceitarTermos.setOnClickListener {
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
        }
    }

    private fun adcionarProfissional(): Task<String> {
        return FirebaseFunctions
            .getInstance("southamerica-east1")
            .getHttpsCallable("salvarDadosPessoais")
            .call(intent.data)
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }
}