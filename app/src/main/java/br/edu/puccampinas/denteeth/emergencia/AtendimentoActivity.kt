package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.databinding.ActivityAtendimentoBinding

class AtendimentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtendimentoBinding

    var clicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAtendimentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val telefone = intent.getStringExtra("telefone")
        binding.tvNumeroTelefone.text = telefone

        val nome = intent.getStringExtra("nome")
        binding.tvNomeSocorrista.text = "Ligue para $nome"

        binding.btncall.setOnClickListener {
            clicked = true
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefone")
            startActivity(intent)
        }

        binding.btnEnviarLocalizacao.setOnClickListener {
            val intentMaps = Intent(binding.root.context, MapsActivity::class.java)
            this.startActivity(intentMaps)
        }
    }

    override fun onStart() {
        super.onStart()

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTemp.setText("" + millisUntilFinished / 1000)
                if(clicked == true){
                    binding.tvTempoRestante.text = "Ligação concluída com sucesso!"
                    binding.tvTemp.setText("")
                    cancel()
                }
            }

            override fun onFinish() {
                binding.tvTemp.setText("Expirou")

                val intentreturn = Intent(binding.root.context, TelaPrincipalActivity::class.java)
                startActivity(intentreturn)

            }
        }.start()
    }
}