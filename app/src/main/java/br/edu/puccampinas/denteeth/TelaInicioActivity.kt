package br.edu.puccampinas.denteeth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.edu.puccampinas.denteeth.databinding.ActivityTelaInicioBinding
import com.google.android.material.snackbar.Snackbar

class TelaInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializaçâo do Intent para a tela de Registro
        val intentCriarConta = Intent(this, CriarContaActivity::class.java)

        binding.btnEntrar.setOnClickListener {
            hideSoftKeyboard(binding.btnEntrar)

            if (binding.etEmail.text?.isEmpty() == true) {
                Snackbar.make(binding.root, "Campo de E-mail vazio.", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.etSenha.text?.isEmpty() == true) {
                Snackbar.make(binding.root, "Campo de Senha vazio.", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }

        binding.btnRegistrar.setOnClickListener {
            hideSoftKeyboard(binding.btnRegistrar)

            this.startActivity(intentCriarConta)
        }
    }

    private fun hideSoftKeyboard(v: View) {

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

//    private fun checkEmptyTextInput(input: TextInputLayout) {
//        if (input.isEmpty()) {
//            Snackbar.make(binding.root, "Campo de ${input.labelFor} vazio.", Snackbar.LENGTH_LONG).show()
//        } else {}
//    }
}