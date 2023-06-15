package br.edu.puccampinas.denteeth.emergencia

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.Second2Fragment
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.databinding.ActivityFinalizarAtendimentoBinding

class FinalizarAtendimentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalizarAtendimentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalizarAtendimentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnProximaTela.setOnClickListener {
            val proximaTela = Intent(this, TelaPrincipalActivity::class.java)
            startActivity(proximaTela)
        }
    }
}
