package br.edu.puccampinas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView

class TelaInicioActivity : AppCompatActivity() {

    private lateinit var tvRegistrar : AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicio)

        //Inicializaçâo do Intent para a tela de Registro
        //O Intent está redirecionando para si mesmo, deve ser trocado para a tela de registrar
        var intentTelaRegistro = Intent(this, TelaInicioActivity::class.java)

        tvRegistrar = findViewById(R.id.tvRegistrar)

        tvRegistrar.setOnClickListener {
            this.startActivity(intentTelaRegistro)
        }
    }
}