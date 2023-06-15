package br.edu.puccampinas.denteeth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.databinding.ActivityReputacaoRevisaoBinding
import br.edu.puccampinas.denteeth.emergencia.AtenderEmergenciaActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class ReputacaoRevisaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReputacaoRevisaoBinding
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReputacaoRevisaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNomePessoa.text = intent.getStringExtra("notaAvaliacao")
        binding.tvTextoSocorrista.text = intent.getStringExtra("textoAvaliacao")

        binding.btnConcluirSolicitacao.setOnClickListener {
            reportarAvaliacao().addOnCompleteListener { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        binding.root,
                        "Solicitação realizada com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).show()

                    val intentTelaPrincipal = Intent(binding.root.context, TelaPrincipalActivity::class.java)
                    this.startActivity(intentTelaPrincipal)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Ocorreu um erro, tente novamente!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.btnMudeiDeIdeia.setOnClickListener {
            val intentTelaPrincipal = Intent(binding.root.context, TelaPrincipalActivity::class.java)
            this.startActivity(intentTelaPrincipal)
        }
    }

    private fun reportarAvaliacao(): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")
        auth = Firebase.auth

        val dadosReavaliacao = hashMapOf(
            "profissional" to auth.currentUser!!.uid,
            "mensagemProf" to binding.etMotivoRevisaoTexto.text.toString(),
            "avaliacaoId" to intent.getStringExtra("id")
        )

        return functions
            .getHttpsCallable("notificarReavaliacao")
            .call(dadosReavaliacao)
            .continueWith { task ->

                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}