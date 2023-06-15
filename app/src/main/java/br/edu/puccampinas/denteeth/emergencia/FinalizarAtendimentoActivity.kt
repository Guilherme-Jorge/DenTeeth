package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.classes.CustomResponse
import br.edu.puccampinas.denteeth.databinding.ActivityFinalizarAtendimentoBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class FinalizarAtendimentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarAtendimentoBinding

    private lateinit var functions: FirebaseFunctions
    private lateinit var auth: FirebaseAuth
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalizarAtendimentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnProximaTela.setOnClickListener {

            finalizarAtendimento(
                intent.getStringExtra("fcmToken")!!
            ).addOnCompleteListener { res ->
                if (res.result.status == "SUCCESS") {
                    Snackbar.make(
                        binding.root,
                        "Atendimento finalizado com sucesso!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    val proximaTela = Intent(this, TelaPrincipalActivity::class.java)
                    startActivity(proximaTela)
                }
            }
        }
    }

    private fun finalizarAtendimento(fcmToken: String): Task<CustomResponse> {
        auth = Firebase.auth
        val user = auth.currentUser

        functions = Firebase.functions("southamerica-east1")

        val dadosProfissional = hashMapOf(
            "fcmToken" to fcmToken,
            "profissional" to user!!.uid
        )

        return functions
            .getHttpsCallable("notificarAvaliacao")
            .call(dadosProfissional)
            .continueWith { task ->
                val result =
                    gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }
}
