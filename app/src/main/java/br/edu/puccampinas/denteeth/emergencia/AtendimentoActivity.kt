package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.puccampinas.denteeth.databinding.ActivityAtendimentoBinding

class AtendimentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtendimentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAtendimentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val telefone = intent.getStringExtra("telefone")
        binding.tvNumeroTelefone.text = telefone

        val nome = intent.getStringExtra("nome")
        binding.tvNomeSocorrista.text = "Ligue para $nome"

        binding.btncall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telefone")
            startActivity(intent)
        }

        binding.btnEnviarLocalizacao.setOnClickListener {
            val intentMaps = Intent(binding.root.context, MapsActivity::class.java)
            this.startActivity(intentMaps)
        }
    }
}