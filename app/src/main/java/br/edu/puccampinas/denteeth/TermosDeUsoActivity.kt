package br.edu.puccampinas.denteeth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.edu.puccampinas.denteeth.databinding.ActivityTermosdeusoBinding
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.android.gms.tasks.Task

class TermosDeUsoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermosdeusoBinding
    private val TAG = "DenTeeth Firebase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermosdeusoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAceitarTermos.setOnClickListener {
//            adcionarProfissional()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "Usuário salvo com sucesso: ${task.result}")
//                    } else {
//                        val e = task.exception
//                        if (e is FirebaseFunctionsException) {
//                            Log.e(TAG, "Erro ao salvar usuário: [${e.code}] ${e.details}")
//                        } else {
//                            Log.w(TAG, "Erro desconhecido: ${task.exception}")
//                        }
//                    }
//                }
//        }
            Toast.makeText(this, "Usuário salvo com sucesso", Toast.LENGTH_LONG).show()
        }
    }

//    private fun adcionarProfissional(): Task<String> {
//        val dadosPessoais = intent.getSerializableExtra("profissional")
//
//        return FirebaseFunctions
//            .getInstance("southamerica-east1")
//            .getHttpsCallable("salvarDadosPessoais")
//            .call(intent.data)
//            .continueWith { task ->
//                val result = task.result?.data as String
//                result
//            }
//    }
}