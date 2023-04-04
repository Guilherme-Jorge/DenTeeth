package br.edu.puccampinas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import br.edu.puccampinas.myapplication.databinding.ActivityTelaInicioBinding

class TelaInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializaçâo do Intent para a tela de Registro
        val intentTelaRegistro = Intent(this, RegistroActivity::class.java)

        binding.tvRegistrar.setOnClickListener {
            hideSoftKeyboard(binding.tvRegistrar)

            this.startActivity(intentTelaRegistro)
        }
    }

    private fun hideSoftKeyboard(v: View) {

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}