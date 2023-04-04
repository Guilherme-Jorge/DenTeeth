package br.edu.puccampinas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.edu.puccampinas.myapplication.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirmarDados.setOnClickListener {
            hideSoftKeyboard(binding.btnConfirmarDados)

            val intentTermosDeUso = Intent(this, TermosDeUsoActivity::class.java)

            intentTermosDeUso.putExtra("profissional", dadosProfissional())

            this.startActivity(intentTermosDeUso)
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
}